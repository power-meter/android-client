package io.mochahub.powermeter.models

import java.time.Instant
import java.util.UUID

data class WorkoutSession(
    val id: String = UUID.randomUUID().toString(),
    val date: Instant = Instant.now(),
    val workouts: List<Workout>
)

fun WorkoutSession.setDate(epochSecondDate: Long): WorkoutSession {
    return this.copy(date = Instant.ofEpochSecond(epochSecondDate))
}

fun WorkoutSession.setDate(newDate: Instant): WorkoutSession {
    return this.copy(date = newDate)
}

fun WorkoutSession.removeWorkout(workout: Workout): WorkoutSession {
    val workoutList = workouts.toMutableList().apply { this.remove(workout) }
    return this.copy(workouts = workoutList)
}

fun WorkoutSession.addWorkout(workout: Workout): WorkoutSession {
    val workoutList = workouts.toMutableList().apply { this.add(workout) }
    return this.copy(workouts = workoutList)
}
