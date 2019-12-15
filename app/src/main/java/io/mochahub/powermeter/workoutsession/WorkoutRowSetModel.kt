package io.mochahub.powermeter.workoutsession

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.textfield.TextInputEditText
import io.mochahub.powermeter.R
import io.mochahub.powermeter.models.WorkoutSet
import io.mochahub.powermeter.shared.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.row_workout_set_edit)
abstract class WorkoutRowSetModel(
    @EpoxyAttribute var workoutSet: WorkoutSet
) : EpoxyModelWithHolder<WorkoutRowSetModel.Holder>() {

    override fun bind(holder: Holder) {
        holder.repsEditText.setText(workoutSet.reps.toString())
        holder.weightEditText.setText(workoutSet.weight.toString())
        // TODO: On focus change call a callback to get the new set data
    }

    class Holder : KotlinEpoxyHolder() {
        val repsEditText by bind<TextInputEditText>(R.id.newWorkoutSetRepsNumber)
        val weightEditText by bind<TextInputEditText>(R.id.newWorkoutSetWeightNumber)
    }
}