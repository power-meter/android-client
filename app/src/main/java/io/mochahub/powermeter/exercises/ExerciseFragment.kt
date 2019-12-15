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
import io.mochahub.powermeter.data.ExerciseEntity
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
    private val exerciseController = ExerciseController { clicked: ExerciseEntity -> onExerciseClick(clicked) }
    private val itemTouchHelper by lazy { ItemTouchHelper(swipeHandler) }

    private val exerciseSharedViewModel by lazy {
        requireActivity().run {
            ViewModelProviders.of(this)[ExerciseSharedViewModel::class.java]
        }
    }

    private val swipeHandler by lazy {
        object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                val deletedExercise: ExerciseEntity = viewModel.removeExercise(position)
                Snackbar.make(viewHolder.itemView, getString(R.string.exercise_deleted), Snackbar.LENGTH_LONG)
                    .apply {
                        setAction(getString(R.string.undo)) { viewModel.addExercise(deletedExercise) }
                        setActionTextColor(Color.YELLOW)
                        show()
                    }
            }
        }
    }

    private fun onExerciseClick(exercise: ExerciseEntity) {
        exerciseSharedViewModel.editExercise
        val action = ExerciseFragmentDirections
            .actionDestinationExercisesScreenToExerciseDialog(
                exerciseId = exercise.id,
                exerciseName = exercise.name,
                exercisePR = exercise.personalRecord.toFloat(),
                muscleGroup = exercise.muscleGroup,
                shouldEdit = true
            )
        navController.navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = resources.getString(R.string.exercise_screen_label)

        recyclerView.setController(exerciseController)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        addExerciseBtn.setOnClickListener {
            val action = ExerciseFragmentDirections
                .actionDestinationExercisesScreenToExerciseDialog(
                    exerciseName = null,
                    muscleGroup = null,
                    shouldEdit = false
                )
            navController.navigate(action)
        }

        viewModel.exercises.observe(viewLifecycleOwner, Observer {
            exerciseController.setData(it ?: listOf())
        })

        exerciseSharedViewModel.newExercise.observe(viewLifecycleOwner, Observer {
            it?.let { newExercise ->
                val currentExercises = viewModel.exercises.value ?: listOf()
                val currentExerciseNames = currentExercises.map { exercise -> exercise.name }
                if (currentExerciseNames.contains(newExercise.name)) {
                    Toast.makeText(requireContext(), getString(R.string.alert_exercise_exists), Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.addExercise(newExercise)
                }
                exerciseSharedViewModel.clearNewExercise()
            }
        })

        exerciseSharedViewModel.editExercise.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.updateExercise(it)
                exerciseSharedViewModel.clearEditExercise()
            }
        })
    }
}
