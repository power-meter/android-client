package io.mochahub.powermeter.models

import io.mochahub.powermeter.models.WorkoutSet.Companion.REP_MAX_LIMIT
import io.mochahub.powermeter.models.WorkoutSet.Companion.REP_MIN_LIMIT

data class WorkoutSet(
    val weight: Double,
    val reps: Int = 0
) {
    companion object {
        const val REP_MAX_LIMIT = 100 // For now
        const val REP_MIN_LIMIT = 0
    }

    operator fun plus(increment: Int): WorkoutSet {
        return this.copy(reps = if (reps + increment <= REP_MAX_LIMIT) reps + increment else reps)
    }

    operator fun inc(): WorkoutSet {
        return this + 1
    }

    operator fun minus(decrement: Int): WorkoutSet {
        return this.copy(reps = if (reps - decrement >= REP_MIN_LIMIT) reps - decrement else reps)
    }

    operator fun dec(): WorkoutSet {
        return this - 1
    }
}

fun WorkoutSet.setReps(reps: Int): WorkoutSet {
    return this.copy(reps = if (reps in REP_MIN_LIMIT..REP_MAX_LIMIT) reps else this.reps)
}

fun WorkoutSet.setWeight(weight: Double): WorkoutSet {
    return this.copy(weight = if (weight > 0) weight else this.weight)
}