package io.mochahub.powermeter.models

import io.mochahub.powermeter.data.Exercise as ExerciseDataModel

data class Exercise(
    val name: String,
    val personalRecord: Double,
    val muscleGroup: String // TODO: Make MuscleGroup data/enum class?
) {
    override fun toString(): String {
        return "Exercise(name='$name', personalRecord='$personalRecord' muscleGroup='$muscleGroup')"
    }
}

/**
 *  Not entirely sure how I feel about the db model and the existing model but this extension function
 *  is a useful mapper from one to the other
 */
fun Exercise.toDataModel(): ExerciseDataModel {
    return ExerciseDataModel(name = this.name, personalRecord = this.personalRecord, muscleGroup = this.muscleGroup)
}