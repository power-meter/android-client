package io.mochahub.powermeter.exercises

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.mochahub.powermeter.R
import io.mochahub.powermeter.exercises.ExerciseAdapter.ExerciseViewHolder
import kotlinx.android.synthetic.main.exercise_row.view.*

class ExerciseAdapter(
    private var exercises: List<String>,
    val clickListener: (String) -> Unit
) : Adapter<ExerciseViewHolder>() {

    class ExerciseViewHolder(val view: CardView) : ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.exercise_row, parent, false) as CardView
        return ExerciseViewHolder(view)
    }

    override fun getItemCount(): Int = exercises.size

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.view.textView.text = exercises[position]
        holder.view.setOnClickListener { clickListener(exercises[position]) }
    }

    fun setData(newExercises: List<String>) {
        exercises = newExercises
        notifyDataSetChanged()
    }
}
