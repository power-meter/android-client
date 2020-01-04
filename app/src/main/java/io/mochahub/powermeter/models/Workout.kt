package io.mochahub.powermeter.models

data class Workout(
    val exercise: Exercise,
    val sets: List<WorkoutSet>
)

fun Workout.addSet(set: WorkoutSet): Workout = this.copy(sets = sets + listOf(set))

fun Workout.removeSet(index: Int): Workout {
    val setsList = sets.toMutableList().apply { this.removeAt(index) }
    return this.copy(sets = setsList)
}

fun Workout.updateSet(index: Int, set: WorkoutSet): Workout {
    val setsList = sets.toMutableList().apply { this[index] = set }
    return this.copy(sets = setsList)
}
