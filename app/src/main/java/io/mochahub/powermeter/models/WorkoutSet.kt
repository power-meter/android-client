package io.mochahub.powermeter.models

import io.mochahub.powermeter.models.WorkoutSet.Companion.REP_MAX_LIMIT
import io.mochahub.powermeter.models.WorkoutSet.Companion.REP_MIN_LIMIT
import java.util.UUID

data class WorkoutSet(
    val id: String = UUID.randomUUID().toString(),
    val weight: Double,
    val reps: Int = 0
) {
    companion object {
        const val REP_MAX_LIMIT = 100 // For now
        const val REP_MIN_LIMIT = 0
    }
}

fun WorkoutSet.setReps(reps: Int): WorkoutSet {
    return this.copy(reps = if (reps in REP_MIN_LIMIT..REP_MAX_LIMIT) reps else this.reps)
}

fun WorkoutSet.setWeight(weight: Double): WorkoutSet {
    return this.copy(weight = if (weight > 0) weight else this.weight)
}