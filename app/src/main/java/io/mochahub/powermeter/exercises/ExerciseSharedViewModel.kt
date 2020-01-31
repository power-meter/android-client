package io.mochahub.powermeter.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.mochahub.powermeter.data.exercise.ExerciseEntity

class ExerciseSharedViewModel : ViewModel() {
    private val _newExercise = MutableLiveData<ExerciseEntity>()
    val newExercise: LiveData<ExerciseEntity> = _newExercise

    private val _editExercise = MutableLiveData<ExerciseEntity>()
    val editExercise: LiveData<ExerciseEntity> = _editExercise

    fun saveNewExercise(exercise: ExerciseEntity) {
        _newExercise.postValue(exercise)
    }

    fun clearNewExercise() {
        _newExercise.postValue(null)
    }

    fun saveEditExercise(exercise: ExerciseEntity) {
        _editExercise.postValue(exercise)
    }

    fun clearEditExercise() {
        _editExercise.postValue(null)
    }
}
