package io.mochahub.powermeter.models

data class WorkoutSet (
    val weight: Double,
    val reps: Int = 0
) {
    operator fun plus(increment: Int): WorkoutSet {
        return this.copy(reps = if (reps + increment <= 100) reps + increment else reps)
    }

    operator fun inc(): WorkoutSet {
        return this + 1
    }

    operator fun minus(decrement: Int): WorkoutSet {
        return this.copy(reps = if (reps - decrement >= 0) reps - decrement else reps)
    }

    operator fun dec(): WorkoutSet {
        return this - 1
    }

    fun setReps(reps: Int): WorkoutSet {
        return this.copy(reps = if (reps >= 0) reps else this.reps)
    }
}