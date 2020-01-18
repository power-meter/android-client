package io.mochahub.powermeter.workoutsession

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import io.mochahub.powermeter.data.WorkoutSessionDao
import io.mochahub.powermeter.data.WorkoutSessionEntity
import kotlinx.coroutines.launch

class WorkoutSessionViewModel(private val workoutSessionDao: WorkoutSessionDao) : ViewModel() {
    val workoutSessions: LiveData<List<WorkoutSessionEntity>> =
        workoutSessionDao.getAll().asLiveData()

    fun removeWorkoutSession(position: Int): WorkoutSessionEntity {
        val workoutSession = workoutSessions.value!![position]
        viewModelScope.launch {
            workoutSessionDao.delete(workoutSession)
        }
        return workoutSession
    }
}
