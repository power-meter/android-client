package io.mochahub.powermeter.workoutsession

import android.widget.ArrayAdapter
import com.airbnb.epoxy.TypedEpoxyController
import io.mochahub.powermeter.models.Workout

class WorkoutController(private val arrayAdapter: ArrayAdapter<String>) : TypedEpoxyController<List<Workout>>() {
    override fun buildModels(workouts: List<Workout>?) {
        workouts?.forEach {
            workoutRow(it, arrayAdapter) {
                id(it.exercise.name)
            }
        }
    }
}