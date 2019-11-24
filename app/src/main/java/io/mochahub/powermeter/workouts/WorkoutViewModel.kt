package io.mochahub.powermeter.workouts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.Instant
import java.time.format.DateTimeFormatter

class WorkoutViewModel : ViewModel() {
    val _workouts = MutableLiveData<List<String>>(
        listOf(
            "Workout #1",
            "Workout #2"
        )
    )
    

    val workouts: LiveData<List<String>> = _workouts

    fun addWorkout(
        workoutName: String = DateTimeFormatter
            .ofPattern("LLL dd yyyy (E) - HH:mm")
            .format(Instant.now())
    ) {
        val currentList = workouts.value ?: listOf()
        val newList = currentList.toMutableList()
        newList.add(workoutName)
        _workouts.postValue(newList)
    }
}
