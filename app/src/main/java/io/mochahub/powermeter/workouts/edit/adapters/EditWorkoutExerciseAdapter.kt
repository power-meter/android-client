package io.mochahub.powermeter.workouts.edit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.mochahub.powermeter.R
import io.mochahub.powermeter.models.Workout
import kotlinx.android.synthetic.main.row_workout_edit.view.*

private val viewPool = RecyclerView.RecycledViewPool()

class EditWorkoutExerciseAdapter(
    private var workouts: List<Workout>
) : RecyclerView.Adapter<EditWorkoutExerciseAdapter.WorkoutViewHolder>() {

    init {
        workouts.forEach {
            it.expanded = false
        }
    }

    class WorkoutViewHolder(val view: LinearLayout) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_workout_edit, parent, false) as LinearLayout
        return WorkoutViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = workouts.size

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val expanded = workouts[position].expanded

        // TODO(ZAHIN): Animate slide up / down
        // TODO(ZAHIN): Rotate icon
        holder.view.set_recyclerView.visibility = if (expanded) View.VISIBLE else View.GONE
        holder.view.workout_edit_exercise.setText(workouts[position].exercise.name)

        val childLayoutManager = LinearLayoutManager(
            holder.view.set_recyclerView.context, RecyclerView.VERTICAL, false)
        holder.view.set_recyclerView.apply {
            layoutManager = childLayoutManager
            adapter = EditWorkoutSetAdapter(workouts[position].sets)
            setRecycledViewPool(viewPool)
        }

        holder.view.minimize.setEndIconOnClickListener {
            workouts[position].expanded = !workouts[position].expanded
            notifyItemChanged(position)
        }
    }

    fun setData(workouts: List<Workout>) {
        this.workouts = workouts
        notifyDataSetChanged()
    }
}