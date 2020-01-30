package io.mochahub.powermeter.exercises

import com.airbnb.epoxy.TypedEpoxyController
import io.mochahub.powermeter.data.Exercise.ExerciseEntity

class ExerciseController(
    private val clickListener: (ExerciseEntity) -> Unit
) : TypedEpoxyController<List<ExerciseEntity>>() {

    override fun buildModels(exercises: List<ExerciseEntity>?) {
        exercises?.forEach {
            exerciseRow(it, clickListener) {
                id(it.name)
            }
        }
    }
}
