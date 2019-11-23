package io.mochahub.powermeter.models


data class StatsCard(
    var title: String,
    var exercise: Exercise

) {

    override fun toString(): String {
        return "StatsCard(title='$title', body='$exercise')"
    }


}
