package io.mochahub.powermeter.workouts.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.mochahub.powermeter.models.Exercise
import io.mochahub.powermeter.models.Workout

class AddWorkoutViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val _workouts = MutableLiveData<List<Workout>>(
        listOf(
            Workout(
                Exercise("Bench", 145f, "Chest"),
                sets = listOf()
            ),
            Workout(
                Exercise("Squat", 245f, "Legs"),
                sets = listOf()
            ),
            Workout(
                Exercise("Curls", 30f, "Arms"),
                sets = listOf()
            )
        )
    )

    val workouts: LiveData<List<Workout>> = _workouts
}
