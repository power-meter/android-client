package io.mochahub.powermeter.exercises

import com.airbnb.epoxy.TypedEpoxyController
import io.mochahub.powermeter.data.Exercise

class ExerciseController(private val clickListener: (Exercise) -> Unit) : TypedEpoxyController<List<Exercise>>() {
    override fun buildModels(exercises: List<Exercise>?) {
        exercises?.forEach {
            exerciseRow(it, clickListener) {
                id(it.name)
            }
        }
    }
}
