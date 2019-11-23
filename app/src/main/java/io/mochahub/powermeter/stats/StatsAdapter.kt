package io.mochahub.powermeter.stats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import io.mochahub.powermeter.R
import io.mochahub.powermeter.models.Exercise
import io.mochahub.powermeter.models.StatCard
import kotlinx.android.synthetic.main.row_exercise.view.*
import kotlinx.android.synthetic.main.row_stat.view.*

class StatsAdapter(
    private var statCards: List<StatCard>,
    val clickListener: (StatCard) -> Unit
) : RecyclerView.Adapter<StatsAdapter.StatsViewHolder>() {


    class StatsViewHolder(val view: CardView) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        holder.view.card_title.text = statCards[position].title
        holder.view.card_description.text = statCards[position].exercise.name
        holder.view.setOnClickListener { clickListener(statCards[position]) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsAdapter.StatsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_stat, parent, false) as CardView
        return StatsAdapter.StatsViewHolder(view)
    }

    override fun getItemCount(): Int = statCards.size


    fun setData(newStats: List<StatCard>) {
        statCards = newStats
        notifyDataSetChanged()
    }

}
