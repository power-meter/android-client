package io.mochahub.powermeter.models

import java.util.UUID

data class Workout(
    val id: String = UUID.randomUUID().toString(),
    val exercise: Exercise,
    val sets: MutableList<WorkoutSet>
)

fun Workout.addSet(set: WorkoutSet): Workout = this.copy(sets = (sets + mutableListOf(set)) as MutableList<WorkoutSet>)

fun Workout.addSet(position: Int, set: WorkoutSet): Workout {
    sets.add(position, set)
    return this.copy(sets = this.sets)
}

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
