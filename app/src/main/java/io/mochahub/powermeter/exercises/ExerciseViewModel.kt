package io.mochahub.powermeter.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.data.ExerciseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseViewModel(val db: AppDatabase) : ViewModel() {
    val exercises: LiveData<List<ExerciseEntity>> = db.exerciseDao().getAll()

    fun addExercise(exercise: ExerciseEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            db.exerciseDao().insertAll(exercise)
        }
    }

    fun removeExercise(position: Int): ExerciseEntity {
        val exercise = exercises.value!![position]
        viewModelScope.launch(Dispatchers.IO) {
            db.exerciseDao().delete(exercise)
        }
        return exercise
    }

    fun updateExercise(exercise: ExerciseEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            db.exerciseDao().updateExercise(exercise)
        }
    }
}
