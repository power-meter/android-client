package io.mochahub.powermeter.models

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

    fun setReps(reps: Int): WorkoutSet {
        return this.copy(reps = if (reps in REP_MIN_LIMIT..REP_MAX_LIMIT) reps else this.reps)
    }
    fun setWeight(weight: Double): WorkoutSet {
        return this.copy(weight = weight)
    }
}