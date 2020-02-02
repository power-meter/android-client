package io.mochahub.powermeter.models

import java.util.UUID

data class Workout(
    val id: String = UUID.randomUUID().toString(),
    val exercise: Exercise,
    val sets: MutableList<WorkoutSet>,
    val isSetsVisible: Boolean = true
)

fun Workout.addSet(set: WorkoutSet): Workout {
    sets.add(set)
    return this.copy(sets = this.sets)
}

fun Workout.setVisibility(visible: Boolean): Workout {
    return this.copy(isSetsVisible = visible)
}

fun Workout.removeSet(index: Int): Workout {
    val setsList = sets.toMutableList().apply { this.removeAt(index) }
    return this.copy(sets = setsList)
}

fun Workout.updateExercise(exercise: Exercise): Workout {
    return this.copy(exercise = exercise)
}
