package io.mochahub.powermeter.models

data class Workout(
    val exercise: Exercise,
    val sets: List<WorkoutSet>,
    // TODO(ZAHIN): We do not need to save this state to the db
    var expanded: Boolean = false // State for RecyclerView

) {
    fun addSet(set: WorkoutSet): Workout {
        return this.copy(sets = sets + listOf(set))
    }

    fun removeSet(index: Int): Workout {
        val setsList = sets.toMutableList().apply {
            this.removeAt(index)
        }

        return this.copy(sets = setsList)
    }

    fun updateSet(index: Int, set: WorkoutSet): Workout {
        val setsList = sets.toMutableList().apply {
            this[index] = set
        }

        return this.copy(sets = setsList)
    }
}