package io.mochahub.powermeter.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import io.mochahub.powermeter.data.exercise.ExerciseDao
import io.mochahub.powermeter.data.exercise.ExerciseEntity
import kotlinx.coroutines.launch

class ExerciseViewModel(private val exerciseDao: ExerciseDao) : ViewModel() {
    val exercises: LiveData<List<ExerciseEntity>> = exerciseDao.getAll().asLiveData()

    fun addExercise(exercise: ExerciseEntity) {
        viewModelScope.launch { exerciseDao.insertAll(exercise) }
    }

    fun removeExercise(exercise: ExerciseEntity): ExerciseEntity {
        viewModelScope.launch { exerciseDao.delete(exercise) }
        return exercise
    }

    fun updateExercise(exercise: ExerciseEntity) {
        viewModelScope.launch { exerciseDao.updateExercise(exercise) }
    }
}
