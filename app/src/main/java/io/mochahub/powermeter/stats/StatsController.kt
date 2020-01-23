package io.mochahub.powermeter.stats

import com.airbnb.epoxy.TypedEpoxyController
import io.mochahub.powermeter.models.StatCard

class StatsController(
    private val clickListener: (StatCard) -> Unit
) : TypedEpoxyController<List<StatCard>>() {

    override fun buildModels(statCards: List<StatCard>?) {
        statCards?.forEach {
            statCard(it, clickListener) {
                id(it.hashCode())
            }
        }
    }
}
