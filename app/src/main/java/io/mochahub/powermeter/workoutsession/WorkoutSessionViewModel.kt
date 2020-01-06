package io.mochahub.powermeter.workoutsession

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.data.WorkoutSessionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutSessionViewModel(val db: AppDatabase) : ViewModel() {
    val workoutSessions: LiveData<List<WorkoutSessionEntity>> = db.workoutSessionDao().getAll()

    fun removeWorkoutSession(position: Int): WorkoutSessionEntity {
        val workoutSession = workoutSessions.value!![position]
        viewModelScope.launch(Dispatchers.IO) {
            db.workoutSessionDao().delete(workoutSession)
        }
        return workoutSession
    }
}
