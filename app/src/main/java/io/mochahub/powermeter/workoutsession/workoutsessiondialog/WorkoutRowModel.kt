package io.mochahub.powermeter.workoutsession.workoutsessiondialog

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.button.MaterialButton
import io.mochahub.powermeter.R
import io.mochahub.powermeter.models.Workout
import io.mochahub.powermeter.shared.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.row_workout_edit)
abstract class WorkoutRowModel(
    @EpoxyAttribute var workout: Workout,
    @EpoxyAttribute var arrayAdapter: ArrayAdapter<String>,
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var toggleWorkoutSetVisibility: (toggle: Boolean) -> Unit,
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var onExerciseSelected: (exercise: String) -> Unit
) : EpoxyModelWithHolder<WorkoutRowModel.Holder>() {

    override fun bind(holder: Holder) {

        if (workout.exercise.name.isNotBlank()) {
            holder.workoutExerciseTextView.setText(workout.exercise.name)
        }
        holder.workoutExerciseTextView.setAdapter(arrayAdapter)

        holder.workoutExerciseTextView.setOnItemClickListener { _, _, _, _ ->
            onExerciseSelected(holder.workoutExerciseTextView.text.toString())
        }

        holder.toggleWorkoutSetVisibilityButton.setOnClickListener {
            toggleWorkoutSetVisibility(!workout.isSetsVisible)
        }
        holder.toggleWorkoutSetVisibilityButton.rotation =
            if (workout.isSetsVisible) 180f else 0f
    }

    class Holder : KotlinEpoxyHolder() {
        val workoutExerciseTextView by bind<AutoCompleteTextView>(R.id.newWorkoutExerciseText)
        val toggleWorkoutSetVisibilityButton by bind<MaterialButton>(R.id.toggleWorkoutSetVisibility)
    }
}
