package io.mochahub.powermeter.workoutsession.workoutsessiondialog

import android.app.DatePickerDialog
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.textfield.TextInputEditText
import io.mochahub.powermeter.R
import io.mochahub.powermeter.shared.KotlinEpoxyHolder
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

@EpoxyModelClass(layout = R.layout.row_workout_session_date)
abstract class WorkoutSessionDateModel(
    @EpoxyAttribute var date: Instant,
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var datePickerDialog: DatePickerDialog
) : EpoxyModelWithHolder<WorkoutSessionDateModel.Holder>() {

    override fun bind(holder: Holder) {
        val simpleDateFormat = SimpleDateFormat("MM/dd/yy", Locale.US)
        holder.newWorkoutDateText.setText(
            simpleDateFormat.format(Date.from(date)))

        holder.newWorkoutDateText.setOnClickListener {
            datePickerDialog.show()
        }
    }

    class Holder : KotlinEpoxyHolder() {
        val newWorkoutDateText by bind<TextInputEditText>(R.id.newWorkoutDateText)
    }
}