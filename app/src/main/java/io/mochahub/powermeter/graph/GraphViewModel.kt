package io.mochahub.powermeter.graph

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry

class GraphViewModel : ViewModel() {
    private val _data  = MutableLiveData<List<Entry>>(
        listOf(
            Entry(1f,1f),
            Entry(2f,2f),
            Entry(3f,3f),
            Entry(4f,3f),
            Entry(5f,3f),
            Entry(6f,2f),
            Entry(7f,5f),
            Entry(8f,6f),
            Entry(9f,6f),
            Entry(10f,6f)
        )
    )

    val data : LiveData<List<Entry>> = _data

}
