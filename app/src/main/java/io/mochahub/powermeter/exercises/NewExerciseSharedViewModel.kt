package io.mochahub.powermeter.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.mochahub.powermeter.models.Exercise

class NewExerciseSharedViewModel : ViewModel() {
    private val _newExercise = MutableLiveData<Exercise>()
    val newExercise: LiveData<Exercise> = _newExercise

    fun saveNewExercise(exercise: Exercise) {
        _newExercise.postValue(exercise)
    }
}
