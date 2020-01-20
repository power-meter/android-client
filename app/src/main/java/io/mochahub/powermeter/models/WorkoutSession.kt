package io.mochahub.powermeter.models

import java.time.Instant

data class WorkoutSession(
    val date: Instant = Instant.now(),
    val workouts: List<Workout>
)
