package io.mochahub.powermeter.models

data class Exercise(
    val name: String,
    val personalRecord: Float,
    val muscleGroup: String // TODO: Make MuscleGroup data/enum class?
) {

    override fun toString(): String {
        return "Exercise(name='$name', personalRecord='$personalRecord' muscleGroup='$muscleGroup')"
    }
}