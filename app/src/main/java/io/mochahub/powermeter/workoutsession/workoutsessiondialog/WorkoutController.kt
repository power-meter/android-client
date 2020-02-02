package io.mochahub.powermeter.workoutsession.workoutsessiondialog

import android.app.DatePickerDialog
import android.content.Context
import android.widget.ArrayAdapter
import com.airbnb.epoxy.TypedEpoxyController
import io.mochahub.powermeter.R
import io.mochahub.powermeter.models.Workout
import io.mochahub.powermeter.models.WorkoutSession
import io.mochahub.powermeter.models.WorkoutSet

class WorkoutController(
    private val context: Context,
    private val exercises: List<String>,
    private val datePickerDialog: DatePickerDialog,
    private val callbacks: AdapterCallbacks
) : TypedEpoxyController<WorkoutSession>() {

    interface AdapterCallbacks {
        fun onExerciseSelected(workout: Workout, exercise: String)
        fun onAddSetClicked(workout: Workout)
        fun toggleWorkoutSetVisibility(visible: Boolean, workout: Workout)
        fun onRepTextChanged(workout: Workout, workoutSet: WorkoutSet, value: Int)
        fun onWeightTextChanged(workout: Workout, workoutSet: WorkoutSet, value: Double)
    }

    override fun buildModels(workoutSession: WorkoutSession?) {
        if (workoutSession != null) {
            workoutSessionDate(workoutSession.date, datePickerDialog) {
                id(workoutSession.date.toString())
            }
        }
        workoutSession?.workouts?.forEach { workout ->
            workoutRow(
                workout = workout,
                toggleWorkoutSetVisibility = { visible -> this.callbacks.toggleWorkoutSetVisibility(visible, workout) },
                arrayAdapter = ArrayAdapter(context, R.layout.dropdown_menu_popup_item, exercises),
                addButtonClickListener = { callbacks.onAddSetClicked(workout) },
                onExerciseSelected = { value -> callbacks.onExerciseSelected(workout, value) }) {
                id(workout.id)
            }
            if (workout.isSetsVisible) {
                workout.sets.forEach { workoutSet ->
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
}