package io.mochahub.powermeter.shared.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.mochahub.powermeter.models.Exercise

class GraphSharedViewModel : ViewModel() {

    private val selected = MutableLiveData<Exercise>()

    val exercise: LiveData<Exercise> = selected

    fun select(item: Exercise) {
        selected.value = item
    }
}
