package io.mochahub.powermeter.workoutsession

import android.widget.ArrayAdapter
import com.airbnb.epoxy.TypedEpoxyController
import io.mochahub.powermeter.models.Workout

class WorkoutController(
    private var arrayAdapter: ArrayAdapter<String>,
    private val onWorkoutExerciseChange: (Int) -> Unit,
    private val onWorkoutSetChange: (Int, Int) -> Unit
) : TypedEpoxyController<List<Workout>>() {
    override fun buildModels(workouts: List<Workout>?) {
        workouts?.forEachIndexed { workoutIndex, workout ->
            workoutRow(workout, arrayAdapter) {
                id(workoutIndex.toString() + workout.toString())
            }
            workout.sets.forEachIndexed { setIndex, set ->
                workoutRowSet(set) {
                    id(workoutIndex.toString() + setIndex.toString() + set.toString())
                }
            }
        }
    }

    fun setAdapter(arrayAdapter: ArrayAdapter<String>) {
        this.arrayAdapter = arrayAdapter
    }
}