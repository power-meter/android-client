package io.mochahub.powermeter.exercises

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.mochahub.powermeter.R
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.data.Exercise
import io.mochahub.powermeter.models.toDataModel
import io.mochahub.powermeter.shared.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.fragment_exercise.*

class ExerciseFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exercise, container, false)
    }

    private val navController by lazy { this.findNavController() }
    private val appDatabase by lazy { AppDatabase(requireContext()) }
    private val viewModel by lazy { ExerciseViewModel(db = appDatabase) }
    private val exerciseController = ExerciseController { clicked: Exercise -> onExerciseClick(clicked) }
    private val itemTouchHelper by lazy { ItemTouchHelper(swipeHandler) }

    private val newExerciseSharedViewModel by lazy {
        requireActivity().run {
            ViewModelProviders.of(this)[NewExerciseSharedViewModel::class.java]
        }
    }

    private val swipeHandler by lazy {
        object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                val deletedExercise: Exercise = viewModel.removeExercise(position)
                Snackbar.make(viewHolder.itemView, getString(R.string.exercise_deleted), Snackbar.LENGTH_LONG)
                    .apply {
                        setAction(getString(R.string.undo)) { viewModel.addExercise(deletedExercise) }
                        setActionTextColor(Color.YELLOW)
                        show()
                    }
            }
        }
    }

    private fun onExerciseClick(exercise: Exercise) {
        Toast.makeText(requireContext(), "Clicked: ${exercise.name}", Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = resources.getString(R.string.exercise_screen_label)

        recyclerView.setController(exerciseController)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        addExerciseBtn.setOnClickListener {
            navController.navigate(R.id.action_destination_exercises_screen_to_newExerciseDialog)
        }

        viewModel.exercises.observe(viewLifecycleOwner, Observer {
            exerciseController.setData(it ?: listOf())
        })

        newExerciseSharedViewModel.newExercise.observe(viewLifecycleOwner, Observer {
            it?.let {
                val currentExercises: List<String> = viewModel.exercises.value?.map { exercise -> exercise.name } ?: listOf()
                if (currentExercises.contains(it.name)) {
                    Toast.makeText(requireContext(), getString(R.string.alert_exercise_exists), Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.addExercise(it.toDataModel())
                }
                newExerciseSharedViewModel.clearNewExercise()
            }
        })
    }
}
