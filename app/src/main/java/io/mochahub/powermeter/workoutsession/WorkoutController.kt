package io.mochahub.powermeter.workoutsession

import android.widget.ArrayAdapter
import com.airbnb.epoxy.TypedEpoxyController
import io.mochahub.powermeter.models.Workout

class WorkoutController(
    private var arrayAdapter: ArrayAdapter<String>
) : TypedEpoxyController<List<Workout>>() {
    override fun buildModels(workouts: List<Workout>?) {
        workouts?.forEachIndexed { workoutIndex, workout ->
            workoutRow(workout, arrayAdapter) {
                id(workout.hashCode() + workoutIndex)
            }
            workout.sets.forEachIndexed { workoutSetIndex, workoutSet ->
                workoutRowSet(workoutSet) {
                    id(workoutSet.hashCode() + workoutSetIndex)
                }
            }
        }
    }

    fun setAdapter(arrayAdapter: ArrayAdapter<String>) {
        this.arrayAdapter = arrayAdapter
    }
}