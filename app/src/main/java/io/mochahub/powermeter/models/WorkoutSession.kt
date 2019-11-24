package io.mochahub.powermeter.models

import java.util.*

data class WorkoutSession (
    val name: String?,
    val date: Date,
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
