package io.mochahub.powermeter.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.mochahub.powermeter.R
import io.mochahub.powermeter.models.Card

class StatsViewModel : ViewModel() {
    var toggle: Boolean = false

    val stats = MutableLiveData<List<Card>>(
        listOf(
            Card(
                "Exercise",
                "Squats",
                R.drawable.balloon
            ),
            Card(
                "Exercise",
                "Bench Press",
                R.drawable.balloon
            ),
            Card(
                "Muscle Group",
                "Shoulders",
                R.drawable.balloon
            )
        )
    )
}
