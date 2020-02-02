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
        fun addEmptyWorkoutSet(workout: Workout)
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
                toggleWorkoutSetVisibility = { visible -> callbacks.toggleWorkoutSetVisibility(visible, workout) },
                arrayAdapter = ArrayAdapter(context, R.layout.dropdown_menu_popup_item, exercises),
                onExerciseSelected = { value -> callbacks.onExerciseSelected(workout, value) }) {
                id(workout.id)
            }
            if (workout.isSetsVisible) {
                workout.sets.forEachIndexed { setIndex, workoutSet ->
                    workoutRowSet(
                        workoutSet = workoutSet,
                        lastSet = (setIndex == workout.sets.size - 1),
                        onRepTextChanged = { value ->
                            callbacks.onRepTextChanged(
                                workout,
                                workoutSet,
                                value
                            )
                        },
                        onWeightTextChanged = { value ->
                            callbacks.onWeightTextChanged(
                                workout,
                                workoutSet,
                                value
                            )
                        },
                        lastSetFocused = {
                            callbacks.addEmptyWorkoutSet(workout)
                        }) {
                        id(workoutSet.id)
                    }
                }
            }
        }
    }
}