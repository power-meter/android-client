package io.mochahub.powermeter.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.mochahub.powermeter.models.Exercise

class ExerciseViewModel : ViewModel() {

    val _exercises = MutableLiveData<List<Exercise>>(
        listOf(
            Exercise(name = "Bench Press", personalRecord = 100f, muscleGroup = "Chest"),
            Exercise(name = "Squat", personalRecord = 200.4f, muscleGroup = "Legs"),
            Exercise(name = "Overhead Press", personalRecord = 30f, muscleGroup = "Shoulders")
        )
    )
    val exercises : LiveData<List<Exercise>> = _exercises

    fun addExercise(exercise: Exercise) {
        val currentList = exercises.value ?: listOf()
        val newList = currentList.toMutableList()
        newList.add(exercise)
        _exercises.postValue(newList)
    }
}
