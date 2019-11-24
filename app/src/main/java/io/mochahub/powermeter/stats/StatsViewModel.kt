package io.mochahub.powermeter.stats

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.mochahub.powermeter.models.Exercise
import io.mochahub.powermeter.models.StatCard

class StatsViewModel : ViewModel() {

    val stats = MutableLiveData<List<StatCard>>(
        listOf(
            StatCard(
                "Exercise",
                Exercise("Bench Press", 100f, "Chest")
            ),
            StatCard(
                "Exercise",
                Exercise("Squat", 30f, "Legs")
            ),
            StatCard(
                "Exercise",
                Exercise("Overhead Press", 30f, "Shoulders")
            )
        )
    )
}
