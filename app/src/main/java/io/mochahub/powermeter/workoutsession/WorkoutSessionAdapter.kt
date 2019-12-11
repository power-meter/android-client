package io.mochahub.powermeter.workoutsession

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView.Adapter
import io.mochahub.powermeter.R
import io.mochahub.powermeter.models.WorkoutSession
import kotlinx.android.synthetic.main.row_workout_session.view.*
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class WorkoutSessionAdapter(
    private var workoutSessions: List<WorkoutSession>,
    val clickListener: (WorkoutSession) -> Unit
) : Adapter<WorkoutSessionAdapter.WorkoutSessionViewHolder>() {

    class WorkoutSessionViewHolder(val view: CardView) : ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutSessionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_workout_session, parent, false) as CardView

        return WorkoutSessionViewHolder(view)
    }

    override fun getItemCount(): Int = workoutSessions.size

    override fun onBindViewHolder(holder: WorkoutSessionViewHolder, position: Int) {
        holder.view.dateView.text = DateTimeFormatter.ofPattern("LLL dd yyyy (E) - HH:mm")
            .withZone(ZoneId.systemDefault())
            .format(workoutSessions[position].date)
        holder.view.workoutView.text = "${workoutSessions[position].workouts.size} workouts"
        holder.view.setOnClickListener { clickListener(workoutSessions[position]) }
    }

    fun setData(workoutSessions: List<WorkoutSession>) {
        this.workoutSessions = workoutSessions
        notifyDataSetChanged()
    }
}