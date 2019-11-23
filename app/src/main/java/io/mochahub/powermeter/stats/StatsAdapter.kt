package io.mochahub.powermeter.stats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.mochahub.powermeter.R
import io.mochahub.powermeter.models.Card
import kotlinx.android.synthetic.main.row_stats.view.*

class StatsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var items:List<Card> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CardViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_stats,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder){
            is CardViewHolder ->{
                holder.bind( items.get(position))
            }
        }
    }
    class CardViewHolder constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){
        val card_titlle: TextView = itemView.card_title
        val card_image: ImageView = itemView.card_image
        val card_description: TextView = itemView.card_description

        fun bind(card: Card){
            card_titlle.setText(card.title)
            card_description.setText(card.body)
            card_image.setImageResource(card.image)
        }
    }

    fun setList(items: List<Card>){
        this.items = items
    }
}