package io.mochahub.powermeter.exercises

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import io.mochahub.powermeter.R
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.data.exercise.ExerciseEntity
import io.mochahub.powermeter.shared.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.fragment_exercise.addExerciseBtn
import kotlinx.android.synthetic.main.fragment_exercise.recyclerView
import splitties.toast.toast

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
        ExerciseViewModel(AppDatabase(requireContext()).exerciseDao())
    }
    private val exerciseController =
        ExerciseController { clicked: ExerciseEntity -> onExerciseClick(clicked) }
    private val itemTouchHelper by lazy { ItemTouchHelper(swipeHandler) }

    private val exerciseSharedViewModel by lazy {
        requireActivity().run {
            ViewModelProviders.of(this)[ExerciseSharedViewModel::class.java]
        }
    }

    private var exercises: List<ExerciseEntity> = ArrayList()

    private val swipeHandler by lazy {
        object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                val dialogBuilder = AlertDialog.Builder(ContextThemeWrapper(requireContext(), R.style.Theme_AppCompat_Dialog_Alert))

                val exerciseEntity = (exercises as ArrayList).removeAt(position)
                exerciseController.setData(exercises)

                dialogBuilder.setMessage("Deleting this exercise will delete all workouts associated with this exercise. Are you sure you want to delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                        viewModel.removeExercise(exerciseEntity)
                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener { dialog, _ ->
                        (exercises as ArrayList).add(position, exerciseEntity)
                        exerciseController.setData(exercises)
                        dialog.cancel()
                    })

                val dialog = dialogBuilder.create()
                dialog.setTitle("WARNING!")

                dialog.setOnShowListener { _ ->
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.TRANSPARENT)
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.TRANSPARENT)

                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.YELLOW)
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.YELLOW)
                }

                dialog.show()
            }
        }
    }

    private fun onExerciseClick(exercise: ExerciseEntity) {
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
                    exerciseId = null,
                    exerciseName = null,
                    muscleGroup = null,
                    shouldEdit = false
                )
            navController.navigate(action)
        }

        viewModel.exercises.observe(viewLifecycleOwner, Observer {
            exercises = it ?: emptyList()
            exerciseController.setData(exercises)
        })

        exerciseSharedViewModel.newExercise.observe(viewLifecycleOwner, Observer {
            it?.let {
                addNewExercise(it)
            }
        })

        exerciseSharedViewModel.editExercise.observe(viewLifecycleOwner, Observer {
            it?.let {
                editExercise(it)
            }
        })
    }

    private fun addNewExercise(newExercise: ExerciseEntity) {
        val currentExercises = viewModel.exercises.value ?: emptyList()
        val collision = currentExercises.filter { it.name == newExercise.name }
        if (collision.isNotEmpty()) {
            toast(R.string.alert_exercise_exists)
        } else {
            viewModel.addExercise(newExercise)
        }
        exerciseSharedViewModel.clearNewExercise()
    }

    private fun editExercise(exercise: ExerciseEntity) {
        val currentExercises = viewModel.exercises.value ?: emptyList()
        val collision = currentExercises.filter { it.name == exercise.name && it.id != exercise.id }
        if (collision.isNotEmpty()) {
            toast(R.string.alert_exercise_exists)
        } else {
            viewModel.updateExercise(exercise)
        }
        exerciseSharedViewModel.clearEditExercise()
    }
}
