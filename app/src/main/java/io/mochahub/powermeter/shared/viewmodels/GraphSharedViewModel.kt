package io.mochahub.powermeter.shared.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.mochahub.powermeter.models.Exercise

class GraphSharedViewModel : ViewModel() {

    private val _selectedExercise = MutableLiveData<Exercise>()

    val selectedExercise: LiveData<Exercise> = _selectedExercise

    fun select(item: Exercise) {
        _selectedExercise.postValue(item)
    }
}
