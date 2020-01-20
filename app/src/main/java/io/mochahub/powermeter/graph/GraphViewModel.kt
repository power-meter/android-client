package io.mochahub.powermeter.graph

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GraphViewModel : ViewModel() {
    private val _data = MutableLiveData<List<Float>>(
        listOf(1f, 2f, 3f, 3f, 3f, 2f, 5f, 6f, 6f)
    )

    val data: LiveData<List<Float>> = _data
}
