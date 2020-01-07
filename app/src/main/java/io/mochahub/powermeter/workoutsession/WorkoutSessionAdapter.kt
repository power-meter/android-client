package io.mochahub.powermeter.workoutsession

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView.Adapter
import io.mochahub.powermeter.R
import io.mochahub.powermeter.data.WorkoutSessionEntity
import kotlinx.android.synthetic.main.row_workout_session.view.*
import java.text.SimpleDateFormat
import java.util.Date

class WorkoutSessionAdapter(
    private var workoutSessions: List<WorkoutSessionEntity>,
    val clickListener: (WorkoutSessionEntity) -> Unit
) : Adapter<WorkoutSessionAdapter.WorkoutSessionViewHolder>() {

    private val sdf = SimpleDateFormat("LLL dd yyyy (E)")
    class WorkoutSessionViewHolder(val view: CardView) : ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutSessionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_workout_session, parent, false) as CardView

        return WorkoutSessionViewHolder(view)
    }

    override fun getItemCount(): Int = workoutSessions.size

    override fun onBindViewHolder(holder: WorkoutSessionViewHolder, position: Int) {
        var date = Date(workoutSessions[position].date * 1000L)
        holder.view.dateView.text = sdf.format(date)
        holder.view.workoutView.text = workoutSessions[position].name
        holder.view.setOnClickListener { clickListener(workoutSessions[position]) }
    }

    fun setData(workoutSessions: List<WorkoutSessionEntity>) {
        this.workoutSessions = workoutSessions
        notifyDataSetChanged()
    }
}