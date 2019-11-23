package io.mochahub.powermeter.stats

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.mochahub.powermeter.R
import io.mochahub.powermeter.models.Exercise
import io.mochahub.powermeter.models.StatsCard

class StatsViewModel : ViewModel() {
    var toggle: Boolean = false

    val stats = MutableLiveData<List<StatsCard>>(
        listOf(
            StatsCard(
                "Exercise",
                Exercise("Squats",1.0f,"legs")
            ),
            StatsCard(
                "Exercise",
                Exercise("Bench Press",1.0f,"chest")
            ),
            StatsCard(
                "Exercise",
                Exercise("Shoulder Press",1.0f,"shoulders")
            )
        )
    )
}
