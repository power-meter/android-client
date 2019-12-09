package io.mochahub.powermeter.workouts.edit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import io.mochahub.powermeter.R
import io.mochahub.powermeter.models.WorkoutSet
import kotlinx.android.synthetic.main.row_workout_edit_set.view.*

class EditWorkoutSetAdapter(
    private var workoutSets: List<WorkoutSet>
) : RecyclerView.Adapter<EditWorkoutSetAdapter.WorkoutSetViewHolder>() {

    class WorkoutSetViewHolder(val view: LinearLayout) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutSetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_workout_edit_set, parent, false) as LinearLayout
        return WorkoutSetViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = workoutSets.size

    override fun onBindViewHolder(holder: WorkoutSetViewHolder, position: Int) {
        holder.view.workout_edit_rep.setText(workoutSets[position].reps.toString())
        holder.view.workout_edit_weight.setText(workoutSets[position].weight.toString())
    }

    fun setData(workoutSets: List<WorkoutSet>) {
        this.workoutSets = workoutSets
        notifyDataSetChanged()
    }
}