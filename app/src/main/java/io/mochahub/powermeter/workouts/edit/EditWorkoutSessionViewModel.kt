package io.mochahub.powermeter.workouts.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.mochahub.powermeter.models.Exercise
import io.mochahub.powermeter.models.Workout
import io.mochahub.powermeter.models.WorkoutSet

class EditWorkoutSessionViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val _workouts = MutableLiveData<List<Workout>>(
        listOf(
            Workout(
                Exercise("Bench", 145f, "Chest"),
                sets = listOf(
                    WorkoutSet(135.0, 10),
                    WorkoutSet(135.0, 10),
                    WorkoutSet(135.0, 10)
                )
            ),
            Workout(
                Exercise("Squat", 245f, "Legs"),
                sets = listOf(
                    WorkoutSet(95.0, 10),
                    WorkoutSet(95.0, 10),
                    WorkoutSet(145.0, 10)
                )
            ),
            Workout(
                Exercise("Curls", 30f, "Arms"),
                sets = listOf(
                    WorkoutSet(30.0, 10),
                    WorkoutSet(30.0, 10),
                    WorkoutSet(30.0, 10)
                )
            )
        )
    )

    val workouts: LiveData<List<Workout>> = _workouts
}
