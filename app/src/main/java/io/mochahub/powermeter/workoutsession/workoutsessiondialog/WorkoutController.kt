package io.mochahub.powermeter.workoutsession.workoutsessiondialog

import android.widget.ArrayAdapter
import com.airbnb.epoxy.TypedEpoxyController
import io.mochahub.powermeter.models.Workout

class WorkoutController(
    private var arrayAdapter: ArrayAdapter<String>,
    private val callbacks: AdapterCallbacks
) : TypedEpoxyController<List<Workout>>() {

    interface AdapterCallbacks {
        fun onExerciseSelected(workoutIndex: Int, exercise: String)
        fun onAddSetClicked(index: Int)
        fun onRepFocusChanged(workoutIndex: Int, setIndex: Int, value: Int)
        fun onWeightFocusChanged(workoutIndex: Int, setIndex: Int, value: Double)
    }

    override fun buildModels(workouts: List<Workout>?) {
        workouts?.forEachIndexed { workoutIndex, workout ->
            workoutRow(workout, arrayAdapter,
                { callbacks.onAddSetClicked(workoutIndex) },
                { value -> callbacks.onExerciseSelected(workoutIndex, value) }) {
                id(workout.id)
            }
            workout.sets.forEachIndexed { workoutSetIndex, workoutSet ->
                workoutRowSet(workoutSet,
                    { value -> callbacks.onRepFocusChanged(workoutIndex, workoutSetIndex, value) },
                    { value -> callbacks.onWeightFocusChanged(workoutIndex, workoutSetIndex, value) }) {
                    id(workoutSet.id)
                }
            }
        }
    }

    fun setAdapter(arrayAdapter: ArrayAdapter<String>) {
        this.arrayAdapter = arrayAdapter
    }
}