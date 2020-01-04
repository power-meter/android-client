package io.mochahub.powermeter.models

import java.time.Instant

data class WorkoutSession(
    val name: String? = null,
    val date: Instant = Instant.now(),
    val workouts: List<Workout>
)

fun WorkoutSession.updateWorkout(index: Int, workout: Workout): WorkoutSession {
    val workoutsList = workouts.toMutableList().apply {
        this[index] = workout
    }
    return this.copy(workouts = workoutsList)
}

fun WorkoutSession.removeWorkout(index: Int): WorkoutSession {
    val workoutsList = workouts.toMutableList().apply {
        this.removeAt(index)
    }
    return this.copy(workouts = workoutsList)
}

fun WorkoutSession.addWorkout(workout: Workout): WorkoutSession {
    return this.copy(workouts = workouts + listOf(workout))
}
