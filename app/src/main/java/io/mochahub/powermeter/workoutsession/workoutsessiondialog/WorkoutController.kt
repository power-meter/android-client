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
        fun onRepTextChanged(workout: Workout, workoutSet: WorkoutSet, value: Int)
        fun onWeightTextChanged(workout: Workout, workoutSet: WorkoutSet, value: Double)
    }

    override fun buildModels(workouts: List<Workout>?) {
        workouts?.forEachIndexed { _, workout ->
            workoutRow(
                workout = workout,
                arrayAdapter = ArrayAdapter(context, R.layout.dropdown_menu_popup_item, exercises),
                addButtonClickListener = { callbacks.onAddSetClicked(workout) },
                onExerciseSelected = { value -> callbacks.onExerciseSelected(workout, value) }) {
                id(workout.id)
            }
            workout.sets.forEachIndexed { _, workoutSet ->
                workoutRowSet(
                    workoutSet = workoutSet,
                    onRepFocusChanged = { value ->
                        callbacks.onRepTextChanged(
                            workout,
                            workoutSet,
                            value
                        )
                    },
                    onWeightFocusChanged = { value ->
                        callbacks.onWeightTextChanged(
                            workout,
                            workoutSet,
                            value
                        )
                    }) {
                    id(workoutSet.id)
                }
            }
        }
    }
}