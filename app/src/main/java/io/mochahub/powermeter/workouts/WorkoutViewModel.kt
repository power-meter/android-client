package io.mochahub.powermeter.workouts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.mochahub.powermeter.models.WorkoutSession

class WorkoutViewModel : ViewModel() {
    val _workoutSessions = MutableLiveData<List<WorkoutSession>>(
        listOf(
            WorkoutSession(
                workouts = listOf()
            ),
            WorkoutSession(
                workouts = listOf()
            )
        )
    )

    val workoutSessions: LiveData<List<WorkoutSession>> = _workoutSessions

    fun addWorkoutSession() {
        val currentList = workoutSessions.value ?: listOf()

        _workoutSessions.postValue(listOf(WorkoutSession(workouts = listOf())) + currentList)
    }

    fun restoreWorkoutSession(index: Int, session: WorkoutSession) {
        val updatedList = (workoutSessions.value ?: listOf()).toMutableList().apply {
            add(index, session)
        }

        _workoutSessions.postValue(updatedList)
    }

    fun removeWorkoutSession(index: Int): WorkoutSession {
        val removedWorkoutSession: WorkoutSession
        val updatedList = (workoutSessions.value ?: listOf()).toMutableList().apply {
            removedWorkoutSession = removeAt(index)
        }

        _workoutSessions.postValue(updatedList)

        return removedWorkoutSession
    }
}
