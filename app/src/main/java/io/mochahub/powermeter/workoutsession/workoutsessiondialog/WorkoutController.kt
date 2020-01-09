package io.mochahub.powermeter.workoutsession.workoutsessiondialog

import android.content.Context
import android.widget.ArrayAdapter
import com.airbnb.epoxy.TypedEpoxyController
import io.mochahub.powermeter.R
import io.mochahub.powermeter.models.Workout
import io.mochahub.powermeter.models.WorkoutSet

class WorkoutController(
    private val context: Context,
    private val exercises: List<String>,
    private val callbacks: AdapterCallbacks
) : TypedEpoxyController<List<Workout>>() {

    interface AdapterCallbacks {
        fun onExerciseSelected(workout: Workout, exercise: String)
        fun onAddSetClicked(workout: Workout)
        fun onRepFocusChanged(workout: Workout, workoutSet: WorkoutSet, value: Int)
        fun onWeightFocusChanged(workout: Workout, workoutSet: WorkoutSet, value: Double)
    }

    override fun buildModels(workouts: List<Workout>?) {
        workouts?.forEachIndexed { workoutIndex, workout ->
            workoutRow(workout, ArrayAdapter(context, R.layout.dropdown_menu_popup_item, exercises),
                { callbacks.onAddSetClicked(workout) },
                { value -> callbacks.onExerciseSelected(workout, value) }) {
                id(workout.id)
            }
            workout.sets.forEachIndexed { workoutSetIndex, workoutSet ->
                workoutRowSet(workoutSet, workoutIndex, workoutSetIndex,
                    { value -> callbacks.onRepFocusChanged(workout, workoutSet, value) },
                    { value -> callbacks.onWeightFocusChanged(workout, workoutSet, value) }) {
                    id(workoutSet.id)
                }
            }
        }
    }
}