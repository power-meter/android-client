package io.mochahub.powermeter.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.data.Exercise
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ExerciseViewModel(val db: AppDatabase) : ViewModel() {
    val exercises: LiveData<List<Exercise>> = db.exerciseDao().getAll()

    fun addExercise(exercise: Exercise) {
        GlobalScope.launch {
            db.exerciseDao().insertAll(exercise)
        }
    }
}
