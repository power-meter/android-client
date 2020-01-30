package io.mochahub.powermeter.workoutsession

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import io.mochahub.powermeter.R
import io.mochahub.powermeter.data.WorkoutSession.WorkoutSessionEntity
import io.mochahub.powermeter.shared.KotlinEpoxyHolder
import java.text.SimpleDateFormat
import java.util.Date

@EpoxyModelClass(layout = R.layout.row_workout_session)
abstract class WorkoutSessionRowModel(
    @EpoxyAttribute var workoutSession: WorkoutSessionEntity,
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var clickListener: (WorkoutSessionEntity) -> Unit
) : EpoxyModelWithHolder<WorkoutSessionRowModel.Holder>() {

    override fun bind(holder: Holder) {
        val date = Date(workoutSession.date * 1000L)
        holder.dateView.text = SimpleDateFormat("LLL dd yyyy (E)").format(date)
        holder.view.setOnClickListener { clickListener(workoutSession) }
    }

    class Holder : KotlinEpoxyHolder() {
        val dateView by bind<TextView>(R.id.dateView)
        val workoutView by bind<TextView>(R.id.workoutView)
    }
}