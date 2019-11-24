package io.mochahub.powermeter.workouts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView.Adapter
import io.mochahub.powermeter.models.WorkoutSession

class WorkoutAdapter (
    private var workoutSessions: List<WorkoutSession>,
    val clickListener: (WorkoutSession) -> Unit
) : Adapter<WorkoutAdapter.WorkoutSessionViewHolder>() {

    class WorkoutSessionViewHolder(val view: CardView) : ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutSessionViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int = workoutSessions.size

    override fun onBindViewHolder(holder: WorkoutSessionViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun setData(workoutSessions: List<WorkoutSession>) {
        this.workoutSessions = workoutSessions
        notifyDataSetChanged()
    }
}