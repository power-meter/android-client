package io.mochahub.powermeter.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import io.mochahub.powermeter.data.exercise.ExerciseDao
import io.mochahub.powermeter.data.exercise.ExerciseEntity

class StatsViewModel(private val exerciseDao: ExerciseDao) : ViewModelProvider.Factory, ViewModel() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StatsViewModel(exerciseDao) as T
    }

    val exercises: LiveData<List<ExerciseEntity>> = exerciseDao.getAll().asLiveData()
}
