package io.mochahub.powermeter.models

data class Exercise(
    val name: String,
    val personalRecord: Double,
    val muscleGroup: String // TODO: Make MuscleGroup data/enum class?
)
