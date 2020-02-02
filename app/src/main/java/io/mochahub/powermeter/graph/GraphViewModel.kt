package io.mochahub.powermeter.graph

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import io.mochahub.powermeter.data.workout.WorkoutDao
import io.mochahub.powermeter.data.workout.WorkoutWithRelation

class GraphViewModel(
    private val exerciseID: String,
    private val personalRecord: Double,
    private val workoutDao: WorkoutDao
) : ViewModelProvider.Factory, ViewModel() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GraphViewModel(exerciseID, personalRecord, workoutDao) as T
    }

    val workouts = workoutDao.getAllByExercise(exerciseID).asLiveData()

    private val _data = MutableLiveData<List<Float>>()

    val data: LiveData<List<Float>> = _data

    fun createPowerScore(workouts: List<WorkoutWithRelation>) {
//        [sum for every set (weight/Single Rep PR) ] / total reps
        val scores: ArrayList<Float> = ArrayList()
        workouts.forEach { workout ->
            var workoutScore = 0.0
            workout.workoutSets.forEach { workoutSet ->
                workoutScore += (workoutSet.weight / personalRecord)
            }
            workoutScore /= workout.workoutSets.size
            scores.add(workoutScore.toFloat())
        }
        _data.postValue(scores)
    }
}
