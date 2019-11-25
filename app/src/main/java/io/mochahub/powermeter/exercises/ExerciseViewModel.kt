package io.mochahub.powermeter.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.data.Exercise

class ExerciseViewModel(val db: AppDatabase) : ViewModel() {
    val exercises: LiveData<List<Exercise>> = db.exerciseDao().getAll()

    fun addExercise(exercise: Exercise) {
        db.exerciseDao().insertAll(exercise)
    }
}
