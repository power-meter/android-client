package io.mochahub.powermeter.workoutsession.workoutsessiondialog

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import io.mochahub.powermeter.R
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.models.Exercise
import io.mochahub.powermeter.models.Workout
import io.mochahub.powermeter.models.WorkoutSet
import io.mochahub.powermeter.models.addSet
import io.mochahub.powermeter.models.setReps
import io.mochahub.powermeter.models.setWeight
import io.mochahub.powermeter.models.updateExercise
import kotlinx.android.synthetic.main.dialog_new_workout.addWorkoutBtn
import kotlinx.android.synthetic.main.dialog_new_workout.newWorkoutDateText
import kotlinx.android.synthetic.main.dialog_new_workout.newWorkoutNameText
import kotlinx.android.synthetic.main.dialog_new_workout.newWorkoutToolbar
import kotlinx.android.synthetic.main.fragment_exercise.recyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Calendar
import java.util.Date

class NewWorkoutDialog : WorkoutController.AdapterCallbacks, DialogFragment() {

    private lateinit var workoutController: WorkoutController
    private lateinit var viewModel: NewWorkoutViewModel
    var workouts = ArrayList<Workout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            R.style.FullScreenDialog
        )
    }

    override fun onStart() {
        super.onStart()

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog!!.window!!.setLayout(width, height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_new_workout, container, false)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.saveWorkoutSession(
                newWorkoutNameText.text.toString(), newWorkoutDateText.text.toString(), workouts)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =
            NewWorkoutViewModel(
                db = AppDatabase(requireContext())
            )
        workoutController =
            WorkoutController(
                ArrayAdapter(
                    requireContext(),
                    R.layout.dropdown_menu_popup_item
                ), this
            )

        initFields()
        initDatePicker()

        newWorkoutToolbar.apply {
            title = resources.getString(if (workouts.isEmpty()) R.string.new_workout else R.string.edit_workout)
            setNavigationOnClickListener { dismiss() }
        }

        // init with an empty workout to edit
        if (workouts.isEmpty()) {
            this.addEmptyWorkout()
        }

        addWorkoutBtn.setOnClickListener {
            this.addEmptyWorkout()
            workoutController.setData(workouts)
        }

        viewModel.exercises.observe(viewLifecycleOwner, Observer {
            val adapter = ArrayAdapter<String>(
                requireContext(), R.layout.dropdown_menu_popup_item,
                viewModel.exercises.value?.map { it.name } ?: listOf())
            workoutController.setAdapter(adapter)
            recyclerView.setController(workoutController)
            workoutController.setData(workouts)
        })
    }
    // //////////////////////////////////////////////////////////////
    // Helpers
    // //////////////////////////////////////////////////////////////

    private fun addEmptyWorkout() {
        val workout = Workout(
            Exercise("", 0.0, ""),
            ArrayList()
        )
        workouts.add(0, workout)
    }

    // //////////////////////////////////////////////////////////////
    // Init
    // //////////////////////////////////////////////////////////////
    private fun initFields() {
        // TODO: Set fields from a shared view models
        // This is for when we are editing a workout
    }

    private fun initDatePicker() {

        if (newWorkoutDateText.text.isNullOrEmpty()) {
            newWorkoutDateText.setText(viewModel.sdf.format(Date.from(Instant.now())))
        }

        val myCalendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener {
                _, year, month, day -> myCalendar.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
            newWorkoutDateText.setText(viewModel.sdf.format(myCalendar.time))
        }
        }

        newWorkoutDateText.setOnClickListener {
            DatePickerDialog(requireContext(), dateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    // //////////////////////////////////////////////////////////////
    // Interface methods for workout controller
    // //////////////////////////////////////////////////////////////
    override fun onAddSetClicked(index: Int) {
        workouts[index] = workouts[index].addSet(WorkoutSet(0.0, 0))
        workoutController.setData(workouts)
    }

    override fun onRepFocusChanged(workoutIndex: Int, setIndex: Int, value: Int) {
        workouts[workoutIndex].sets[setIndex] = workouts[workoutIndex].sets[setIndex].setReps(value)
        workoutController.setData(workouts)
    }

    override fun onWeightFocusChanged(workoutIndex: Int, setIndex: Int, value: Double) {
        workouts[workoutIndex].sets[setIndex] = workouts[workoutIndex].sets[setIndex].setWeight(value)
        workoutController.setData(workouts)
    }

    override fun onExerciseSelected(workoutIndex: Int, exercise: String) {
        val exercises = viewModel.exercises.value
        exercises?.find { it ->
            if (it.name == exercise) {
                workouts[workoutIndex] = workouts[workoutIndex]
                    .updateExercise(Exercise(it.name, it.personalRecord, it.muscleGroup))
                workoutController.setData(workouts)
                true
            } else {
                false
            }
        }
    }
}