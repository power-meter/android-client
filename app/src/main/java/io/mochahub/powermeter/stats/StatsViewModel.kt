package io.mochahub.powermeter.stats

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.mochahub.powermeter.R
import io.mochahub.powermeter.models.StatsCard

class StatsViewModel : ViewModel() {
    var toggle: Boolean = false

    val stats = MutableLiveData<List<StatsCard>>(
        listOf(
            StatsCard(
                "Exercise",
                "Squats",
                R.drawable.balloon
            ),
            StatsCard(
                "Exercise",
                "Bench Press",
                R.drawable.balloon
            ),
            StatsCard(
                "Muscle Group",
                "Shoulders",
                R.drawable.balloon
            )
        )
    )
}
