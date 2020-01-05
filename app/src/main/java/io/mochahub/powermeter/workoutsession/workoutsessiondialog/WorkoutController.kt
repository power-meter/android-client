package io.mochahub.powermeter.workoutsession.workoutsessiondialog

import android.widget.ArrayAdapter
import com.airbnb.epoxy.TypedEpoxyController
import io.mochahub.powermeter.models.Workout

class WorkoutController(
    private var arrayAdapter: ArrayAdapter<String>,
    private val callbacks: AdapterCallbacks
) : TypedEpoxyController<List<Workout>>() {

    interface AdapterCallbacks {
        fun onAddSetClicked(index: Int)
        fun onRepFocusChanged(workoutIndex: Int, setIndex: Int, value: Int)
        fun onWeightFocusChanged(workoutIndex: Int, setIndex: Int, value: Double)
    }

    override fun buildModels(workouts: List<Workout>?) {
        workouts?.forEachIndexed { workoutIndex, workout ->
            workoutRow(workout, arrayAdapter, {
                callbacks.onAddSetClicked(workoutIndex)
            }) {
                id(workout.hashCode() + workoutIndex)
            }
            workout.sets.forEachIndexed { workoutSetIndex, workoutSet ->
                workoutRowSet(workoutSet,
                    { value -> callbacks.onRepFocusChanged(workoutIndex, workoutSetIndex, value) },
                    { value -> callbacks.onWeightFocusChanged(workoutIndex, workoutSetIndex, value) }) {
                    id(workoutSet.hashCode() + workoutSetIndex)
                }
            }
        }
    }

    fun setAdapter(arrayAdapter: ArrayAdapter<String>) {
        this.arrayAdapter = arrayAdapter
    }
}