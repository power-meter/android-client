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

    fun addWorkout() {
        val currentList = workoutSessions.value ?: listOf()
        val newList = currentList.toMutableList().apply {
            this.add(WorkoutSession(workouts = listOf()))
        }

        _workoutSessions.postValue(newList)
    }
}
