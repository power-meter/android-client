package io.mochahub.powermeter.models

data class StatCard(
    var title: String,
    var exercise: Exercise

) {

    override fun toString(): String {
        return "StatCard(title='$title', body='$exercise')"
    }
}
