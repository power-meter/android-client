package io.mochahub.powermeter.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExerciseViewModel : ViewModel() {
    // TODO: Replace all of this with calls to Room Dao
    val _exercises = MutableLiveData<List<String>>(
        listOf(
            "Bench Press",
            "Squats",
            "Shoulder Press"
        )
    )
    val exercises : LiveData<List<String>> = _exercises

    fun addExercise(exercise: String) {
        val currentList = exercises.value ?: listOf()
        val newList = currentList.toMutableList()
        newList.add(exercise)
        _exercises.postValue(newList)
    }
}
