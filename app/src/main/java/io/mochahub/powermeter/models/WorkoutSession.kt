package io.mochahub.powermeter.models

import java.time.Instant

data class WorkoutSession(
    val date: Instant = Instant.now(),
    val workouts: List<Workout>
)

fun WorkoutSession.setDate(epochSecondDate: Long): WorkoutSession {
    return this.copy(date = Instant.ofEpochSecond(epochSecondDate))
}

fun WorkoutSession.setDate(newDate: Instant): WorkoutSession {
    return this.copy(date = newDate)
}
