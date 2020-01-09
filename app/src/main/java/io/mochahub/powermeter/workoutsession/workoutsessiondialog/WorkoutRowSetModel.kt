package io.mochahub.powermeter.workoutsession.workoutsessiondialog

import android.text.Editable
import android.text.TextWatcher
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
    @EpoxyAttribute var workoutIndex: Int,
    @EpoxyAttribute var workoutSetIndex: Int,
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var onRepFocusChanged: (reps: Int) -> Unit,
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var onWeightFocusChanged: (weight: Double) -> Unit
) : EpoxyModelWithHolder<WorkoutRowSetModel.Holder>() {

    override fun bind(holder: Holder) {
        if (workoutSet.reps != 0) {
            holder.repsEditText.setText(workoutSet.reps.toString())
        }
        if (workoutSet.weight != 0.0) {
            // We do not want to display 1.0 when the user simply inputs 1
            if (workoutSet.weight % 1 == 0.0) {
                holder.weightEditText.setText(workoutSet.weight.toInt().toString())
            } else {
                holder.weightEditText.setText(workoutSet.weight.toString())
            }
        }

        // Move cursor to the end
        if (holder.repsEditText.hasFocus() && holder.repsEditText.text != null) {
            holder.repsEditText.setSelection(holder.repsEditText.text!!.length)
        }
        if (holder.weightEditText.hasFocus() && holder.weightEditText.text != null) {
            holder.weightEditText.setSelection(holder.weightEditText.length())
        }

        holder.repsEditText.addTextChangedListener(WorkoutSetTextChangeListener {
            if (holder.repsEditText.text.toString().isNotEmpty() &&
                    holder.repsEditText.text.toString().isNotBlank()) {
                onRepFocusChanged(holder.repsEditText.text.toString().toInt())
            }
        })
        holder.weightEditText.addTextChangedListener(WorkoutSetTextChangeListener {
            if (holder.weightEditText.text.toString().isNotEmpty() &&
                    holder.weightEditText.text.toString().isNotBlank()) {
                onWeightFocusChanged(holder.weightEditText.text.toString().toDouble())
            }
        })
    }

    class Holder : KotlinEpoxyHolder() {
        val repsEditText by bind<TextInputEditText>(R.id.newWorkoutSetRepsNumber)
        val weightEditText by bind<TextInputEditText>(R.id.newWorkoutSetWeightNumber)
    }
}

private class WorkoutSetTextChangeListener(
    private val callback: () -> Unit
) : TextWatcher {
    override fun afterTextChanged(s: Editable?) { callback() }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}