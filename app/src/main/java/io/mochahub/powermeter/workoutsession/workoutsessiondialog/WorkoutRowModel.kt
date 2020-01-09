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
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var addButtonClickListener: () -> Unit,
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var onExerciseSelected: (exercise: String) -> Unit
) : EpoxyModelWithHolder<WorkoutRowModel.Holder>() {

    override fun bind(holder: Holder) {
        holder.workoutExerciseTextView.setAdapter(arrayAdapter)

        if (workout.exercise.name.isNotEmpty() || workout.exercise.name.isNotBlank()) {
            holder.workoutExerciseTextView.setText(workout.exercise.name)
        }

        holder.workoutExerciseTextView.setOnItemClickListener { _, _, _, _ ->
            onExerciseSelected(holder.workoutExerciseTextView.text.toString())
        }

        holder.addEmptyWorkoutSetButton.setOnClickListener {
            addButtonClickListener()
        }
    }

    class Holder : KotlinEpoxyHolder() {
        val workoutExerciseTextView by bind<AutoCompleteTextView>(R.id.newWorkoutExerciseText)
        val addEmptyWorkoutSetButton by bind<MaterialButton>(R.id.newWorkoutSetButton)
    }
}
