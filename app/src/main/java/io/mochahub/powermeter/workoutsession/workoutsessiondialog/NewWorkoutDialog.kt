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
import kotlinx.android.synthetic.main.dialog_new_workout.addWorkoutBtn
import kotlinx.android.synthetic.main.dialog_new_workout.newWorkoutDateText
import kotlinx.android.synthetic.main.dialog_new_workout.newWorkoutToolbar
import kotlinx.android.synthetic.main.fragment_exercise.recyclerView
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.Locale

class NewWorkoutDialog : WorkoutController.AdapterCallbacks, DialogFragment() {

    private lateinit var workoutController: WorkoutController
    var workouts = ArrayList<Workout>()
    private val myFormat = "MM/dd/yy"
    private val sdf = SimpleDateFormat(myFormat, Locale.US)

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
        // TODO: Save the workout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel =
            NewWorkoutViewModel(
                db = AppDatabase(requireContext())
            )
        val emptyWorkout = Workout(Exercise("", 0.0, ""), listOf(WorkoutSet(0.0, 0)))
        workoutController =
            WorkoutController(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item
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
            workouts.add(emptyWorkout.copy())
        }

        addWorkoutBtn.setOnClickListener {
            workouts.add(0, emptyWorkout.copy())
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

    // //////////////////////////////////////////////////////////////
    // Init
    // //////////////////////////////////////////////////////////////
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

    // //////////////////////////////////////////////////////////////
    // Interface methods for workout controller
    // //////////////////////////////////////////////////////////////
    override fun onAddSetClicked(index: Int) {
        workouts[index] = workouts[index].addSet(WorkoutSet(0.0, 0))
        workoutController.setData(workouts)
    }

    override fun onRepFocusChanged(workoutIndex: Int, setIndex: Int, value: Int) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun onWeightFocusChanged(workoutIndex: Int, setIndex: Int, value: Double) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}