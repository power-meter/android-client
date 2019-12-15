package io.mochahub.powermeter.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.mochahub.powermeter.data.Exercise

class ExerciseSharedViewModel : ViewModel() {
    private val _newExercise = MutableLiveData<Exercise>()
    val newExercise: LiveData<Exercise> = _newExercise

    private val _editExercise = MutableLiveData<Exercise>()
    val editExercise: LiveData<Exercise> = _editExercise

    fun saveNewExercise(exercise: Exercise) {
        _newExercise.postValue(exercise)
    }

    fun clearNewExercise() {
        _newExercise.postValue(null)
    }

    fun saveEditExercise(exercise: Exercise) {
        _editExercise.postValue(exercise)
    }

    fun clearEditExercise() {
        _editExercise.postValue(null)
    }
}
