package io.mochahub.powermeter.workoutsession

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import io.mochahub.powermeter.data.workout.WorkoutDao
import io.mochahub.powermeter.data.workoutsession.WorkoutSessionDao
import io.mochahub.powermeter.data.workoutsession.WorkoutSessionWithRelation
import io.mochahub.powermeter.data.workoutset.WorkoutSetDao
import io.mochahub.powermeter.data.workoutset.WorkoutSetEntity
import kotlinx.coroutines.launch

class WorkoutSessionViewModel(
    private val workoutSessionDao: WorkoutSessionDao,
    private val workoutDao: WorkoutDao,
    private val workoutSetDao: WorkoutSetDao
) : ViewModel() {
    val workoutSessions: LiveData<List<WorkoutSessionWithRelation>> =
        workoutSessionDao.getAllWithRelations().asLiveData()

    fun removeWorkoutSession(position: Int): WorkoutSessionWithRelation {
        val workoutSession = workoutSessions.value!![position]
        viewModelScope.launch {
            workoutSessionDao.delete(workoutSession.workoutSession.id)
        }
        return workoutSession
    }

    fun insertWorkoutSession(workoutSession: WorkoutSessionWithRelation) {
        val workouts = workoutSession.workouts.map { it.workout }
        val workoutSets: ArrayList<WorkoutSetEntity> = ArrayList()

        workoutSession.workouts.forEach {
            it.workoutSets.forEach { workoutSet ->
                workoutSets.add(workoutSet)
            }
        }
        viewModelScope.launch {
            workoutSessionDao.insertAll(workoutSession.workoutSession)
            workoutDao.insertAll(*(workouts.toTypedArray()))
            workoutSetDao.insertAll(*(workoutSets.toTypedArray()))
        }
    }
}
