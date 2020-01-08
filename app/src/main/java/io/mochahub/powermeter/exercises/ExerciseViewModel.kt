package io.mochahub.powermeter.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.data.ExerciseEntity
import kotlinx.coroutines.launch

class ExerciseViewModel(val db: AppDatabase) : ViewModel() {
    val exercises: LiveData<List<ExerciseEntity>> = db.exerciseDao().getAll().asLiveData()

    fun addExercise(exercise: ExerciseEntity) {
        viewModelScope.launch { db.exerciseDao().insertAll(exercise) }
    }

    fun removeExercise(position: Int): ExerciseEntity {
        val exercise = exercises.value!![position]
        viewModelScope.launch { db.exerciseDao().delete(exercise) }
        return exercise
    }

    fun updateExercise(exercise: ExerciseEntity) {
        viewModelScope.launch { db.exerciseDao().updateExercise(exercise) }
    }
}
