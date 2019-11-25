package io.mochahub.powermeter.exercises

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.mochahub.powermeter.R
import io.mochahub.powermeter.exercises.ExerciseAdapter.ExerciseViewHolder
import io.mochahub.powermeter.data.Exercise
import kotlinx.android.synthetic.main.row_exercise.view.*

class ExerciseAdapter(
    private var exercises: List<Exercise>,
    val clickListener: (Exercise) -> Unit
) : Adapter<ExerciseViewHolder>() {

    class ExerciseViewHolder(val view: CardView) : ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_exercise, parent, false) as CardView
        return ExerciseViewHolder(view)
    }

    override fun getItemCount(): Int = exercises.size

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.view.nameTextView.text = exercises[position].name
        holder.view.personalRecordTextView.text = exercises[position].personalRecord.toString()
        holder.view.muslceTextView.text = exercises[position].muscleGroup
        holder.view.setOnClickListener { clickListener(exercises[position]) }
    }

    fun setData(newExercises: List<Exercise>) {
        exercises = newExercises
        notifyDataSetChanged()
    }
}
