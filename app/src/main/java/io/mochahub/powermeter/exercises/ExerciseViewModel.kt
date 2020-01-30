package io.mochahub.powermeter.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import io.mochahub.powermeter.data.Exercise.ExerciseDao
import io.mochahub.powermeter.data.Exercise.ExerciseEntity
import kotlinx.coroutines.launch

class ExerciseViewModel(private val exerciseDao: ExerciseDao) : ViewModel() {
    val exercises: LiveData<List<ExerciseEntity>> = exerciseDao.getAll().asLiveData()

    fun addExercise(exercise: ExerciseEntity) {
        viewModelScope.launch { exerciseDao.insertAll(exercise) }
    }

    fun removeExercise(position: Int): ExerciseEntity {
        val exercise = exercises.value!![position]
        viewModelScope.launch { exerciseDao.delete(exercise) }
        return exercise
    }

    fun updateExercise(exercise: ExerciseEntity) {
        viewModelScope.launch { exerciseDao.updateExercise(exercise) }
    }
}
