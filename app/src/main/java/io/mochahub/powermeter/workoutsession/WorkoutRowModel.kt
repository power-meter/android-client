package io.mochahub.powermeter.workoutsession

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import io.mochahub.powermeter.R
import io.mochahub.powermeter.models.Workout
import io.mochahub.powermeter.shared.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.row_workout_edit)
abstract class WorkoutRowModel(
    @EpoxyAttribute var workout: Workout,
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var arrayAdapter: ArrayAdapter<String>
) : EpoxyModelWithHolder<WorkoutRowModel.Holder>() {

    override fun bind(holder: Holder) {
        holder.workoutNameView.setAdapter(arrayAdapter)
        // TODO: Figure out a way to get a default value for the spinner
        if (workout.exercise.name.isNotEmpty()) {
            holder.workoutNameView.setSelection(arrayAdapter.getPosition(workout.exercise.name))
        }
    }

    class Holder : KotlinEpoxyHolder() {
        val workoutNameView by bind<AutoCompleteTextView>(R.id.newWorkoutExerciseText)
    }
}