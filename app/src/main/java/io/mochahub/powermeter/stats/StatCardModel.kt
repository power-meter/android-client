package io.mochahub.powermeter.stats

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import io.mochahub.powermeter.R
import io.mochahub.powermeter.models.StatCard
import io.mochahub.powermeter.shared.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.row_stat)
abstract class StatCardModel(
    @EpoxyAttribute var statCard: StatCard,
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var clickListener: (StatCard) -> Unit
) : EpoxyModelWithHolder<StatCardModel.Holder>() {

    override fun bind(holder: Holder) {
        holder.title.text = statCard.title
        holder.description.text = statCard.exercise.name
        holder.view.setOnClickListener { clickListener(statCard) }
    }

    class Holder : KotlinEpoxyHolder() {
        val title by bind<TextView>(R.id.card_title)
        val description by bind<TextView>(R.id.card_description)
    }
}