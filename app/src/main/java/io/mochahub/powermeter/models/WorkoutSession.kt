package io.mochahub.powermeter.models

import java.time.Instant

data class WorkoutSession (
    val name: String? = null,
    val date: Instant = Instant.now(),
    val workouts: List<Workout>
) {
    fun addWorkout(workout: Workout): WorkoutSession {
        val workoutsList = workouts.toMutableList().apply {
            this.add(workout)
        }

        return this.copy(workouts = workoutsList)
    }

    fun removeWorkout(index: Int): WorkoutSession {
        val workoutsList = workouts.toMutableList().apply {
            this.removeAt(index)
        }

        return this.copy(workouts =  workoutsList)
    }

    fun updateWorkout(index: Int, workout: Workout): WorkoutSession {
        val workoutsList = workouts.toMutableList().apply {
            this[index] = workout
        }

        return this.copy(workouts = workoutsList)
    }
}
