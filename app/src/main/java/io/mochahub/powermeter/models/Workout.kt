package io.mochahub.powermeter.models

data class Workout(
    val exercise: Exercise,
    val sets: MutableList<WorkoutSet>
)

fun Workout.addSet(set: WorkoutSet): Workout = this.copy(sets = (sets + mutableListOf(set)) as MutableList<WorkoutSet>)

fun Workout.removeSet(index: Int): Workout {
    val setsList = sets.toMutableList().apply { this.removeAt(index) }
    return this.copy(sets = setsList)
}

fun Workout.updateSet(index: Int, set: WorkoutSet): Workout {
    val setsList = sets.toMutableList().apply { this[index] = set }
    return this.copy(sets = setsList)
}

fun Workout.updateExercise(exercise: Exercise): Workout {
    return this.copy(exercise = exercise)
}
