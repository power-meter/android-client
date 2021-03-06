package io.mochahub.powermeter.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import io.mochahub.powermeter.data.exercise.ExerciseDao
import io.mochahub.powermeter.data.exercise.ExerciseEntity
import kotlinx.coroutines.launch

class ExerciseViewModel(private val exerciseDao: ExerciseDao) : ViewModel() {

    @Suppress("UNCHECKED_CAST")
    class ExerciseViewModelFactory(
        private val exerciseDao: ExerciseDao
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ExerciseViewModel(exerciseDao) as T
        }
    }

    val exercises: LiveData<List<ExerciseEntity>> = exerciseDao.getAll().asLiveData()

    fun removeExercise(exercise: ExerciseEntity): ExerciseEntity {
        viewModelScope.launch { exerciseDao.delete(exercise) }
        return exercise
    }
}
