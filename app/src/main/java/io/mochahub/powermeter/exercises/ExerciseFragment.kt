package io.mochahub.powermeter.exercises

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import io.mochahub.powermeter.R
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.data.exercise.ExerciseEntity
import io.mochahub.powermeter.exercises.ExerciseViewModel.ExerciseViewModelFactory
import io.mochahub.powermeter.shared.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.fragment_exercise.addExerciseBtn
import kotlinx.android.synthetic.main.fragment_exercise.recyclerView

class ExerciseFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exercise, container, false)
    }

    private val navController by lazy { this.findNavController() }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ExerciseViewModelFactory(AppDatabase(requireContext()).exerciseDao())
        ).get(ExerciseViewModel::class.java)
    }

    private val exerciseController =
        ExerciseController { clicked: ExerciseEntity -> goToExerciseDialog(clicked) }

    private val itemTouchHelper by lazy { ItemTouchHelper(swipeHandler) }

    private var exercises: List<ExerciseEntity> = ArrayList()

    private val swipeHandler by lazy {
        object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                val dialogBuilder = AlertDialog.Builder(
                    ContextThemeWrapper(
                        requireContext(),
                        R.style.Theme_AppCompat_Dialog_Alert
                    )
                )

                val exerciseEntity = (exercises as ArrayList).removeAt(position)
                exerciseController.setData(exercises)

                dialogBuilder.setMessage(getString(R.string.exercise_delete_warning))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        viewModel.removeExercise(exerciseEntity)
                    }
                    .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                        (exercises as ArrayList).add(position, exerciseEntity)
                        exerciseController.setData(exercises)
                        dialog.cancel()
                    }

                val dialog = dialogBuilder.create()
                dialog.setTitle(getString(R.string.warning))

                dialog.setOnShowListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setBackgroundColor(Color.TRANSPARENT)
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setBackgroundColor(Color.TRANSPARENT)

                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.YELLOW)
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.YELLOW)
                }

                dialog.show()
            }
        }
    }

    private fun goToExerciseDialog(exercise: ExerciseEntity) {
        val action = ExerciseFragmentDirections
            .actionDestinationExercisesScreenToExerciseDialog(
                exerciseId = exercise.id,
                exerciseName = exercise.name,
                exercisePR = exercise.personalRecord.toFloat(),
                muscleGroup = exercise.muscleGroup
            )
        navController.navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = resources.getString(R.string.exercise_screen_label)

        recyclerView.setController(exerciseController)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        addExerciseBtn.setOnClickListener {
            goToExerciseDialog(ExerciseEntity(name = "", personalRecord = 0.0, muscleGroup = ""))
        }

        viewModel.exercises.observe(viewLifecycleOwner, Observer {
            exercises = it ?: emptyList()
            exerciseController.setData(exercises)
        })
    }
}
