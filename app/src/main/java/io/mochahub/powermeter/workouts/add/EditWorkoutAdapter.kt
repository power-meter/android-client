package io.mochahub.powermeter.workouts.add

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import io.mochahub.powermeter.R
import io.mochahub.powermeter.models.Workout
import kotlinx.android.synthetic.main.row_workout_edit.view.*

class EditWorkoutAdapter(
    private var workouts: List<Workout>
) : RecyclerView.Adapter<EditWorkoutAdapter.WorkoutViewHolder>() {

    class WorkoutViewHolder(val view: CardView) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_workout_edit, parent, false) as CardView
        return WorkoutViewHolder(view)
    }

    override fun getItemCount(): Int = workouts.size

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.view.workout_edit_exercise.setText(workouts[position].exercise.name)
    }

    fun setData(workouts: List<Workout>) {
        this.workouts = workouts
        notifyDataSetChanged()
    }
}