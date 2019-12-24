package io.mochahub.powermeter.workoutsession

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
import kotlinx.android.synthetic.main.dialog_new_workout.addWorkoutBtn
import kotlinx.android.synthetic.main.dialog_new_workout.newWorkoutDateText
import kotlinx.android.synthetic.main.dialog_new_workout.newWorkoutToolbar
import kotlinx.android.synthetic.main.fragment_exercise.recyclerView
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val myFormat = "MM/dd/yy"
private val sdf = SimpleDateFormat(myFormat, Locale.US)

class NewWorkoutDialog : DialogFragment() {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = NewWorkoutViewModel(db = AppDatabase(requireContext()))
        val emptyWorkout = Workout(Exercise("", 0.0, ""), listOf(WorkoutSet(0.0, 0)))
        val workoutController = WorkoutController(ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item))
        var workouts = listOf<Workout>()

        initFields()
        initDatePicker()

        newWorkoutToolbar.apply {
            title = resources.getString(if (workouts.isEmpty()) R.string.new_workout else R.string.edit_workout)
            setNavigationOnClickListener { dismiss() }
        }

        // init with an empty workout to edit
        if (workouts.isEmpty()) {
            workouts = listOf(emptyWorkout.copy())
        }

        addWorkoutBtn.setOnClickListener {
            workouts = listOf(emptyWorkout.copy(), *workouts.toTypedArray())
            workoutController.setData(workouts)
        }

        viewModel.exercises.observe(viewLifecycleOwner, Observer {
            val adapter = ArrayAdapter<String>(
                requireContext(), android.R.layout.simple_spinner_item,
                viewModel.exercises.value?.map { it.name } ?: listOf())
            workoutController.setAdapter(adapter)
            recyclerView.setController(workoutController)
            workoutController.setData(workouts)
        })
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // TODO: Save the workout
    }

    private fun initFields() {
        // TODO: Set fields from a shared view models
        // This is for when we are editing a workout
    }

    private fun initDatePicker() {

        if (newWorkoutDateText.text.isNullOrEmpty()) {
            newWorkoutDateText.setText(sdf.format(Date.from(Instant.now())))
        }

        val myCalendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener {
                _, year, month, day -> myCalendar.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
            newWorkoutDateText.setText(sdf.format(myCalendar.time))
        }
        }

        newWorkoutDateText.setOnClickListener {
            DatePickerDialog(requireContext(), dateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }
}