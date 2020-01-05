package io.mochahub.powermeter.workoutsession.workoutsessiondialog

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.textfield.TextInputEditText
import io.mochahub.powermeter.R
import io.mochahub.powermeter.models.WorkoutSet
import io.mochahub.powermeter.shared.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.row_workout_set_edit)
abstract class WorkoutRowSetModel(
    @EpoxyAttribute var workoutSet: WorkoutSet,
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var onRepFocusChanged: (reps: Int) -> Unit,
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var onWeightFocusChanged: (weight: Double) -> Unit
) : EpoxyModelWithHolder<WorkoutRowSetModel.Holder>() {

    override fun bind(holder: Holder) {
        holder.repsEditText.setText(workoutSet.reps.toString())
        holder.weightEditText.setText(workoutSet.weight.toString())

        holder.repsEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && holder.repsEditText.text.toString().isNotEmpty()) {
                onRepFocusChanged(holder.repsEditText.text.toString().toInt())
            }
        }
        holder.weightEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && holder.weightEditText.toString().isNotEmpty()) {
                onWeightFocusChanged(holder.weightEditText.text.toString().toDouble())
            }
        }
    }

    class Holder : KotlinEpoxyHolder() {
        val repsEditText by bind<TextInputEditText>(R.id.newWorkoutSetRepsNumber)
        val weightEditText by bind<TextInputEditText>(R.id.newWorkoutSetWeightNumber)
    }
}