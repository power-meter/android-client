package io.mochahub.powermeter.workoutsession.workoutsessiondialog

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import io.mochahub.powermeter.R
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.data.ExerciseEntity
import io.mochahub.powermeter.models.Exercise
import io.mochahub.powermeter.models.Workout
import io.mochahub.powermeter.models.WorkoutSession
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
    private val args: NewWorkoutDialogArgs by navArgs()
    private lateinit var workoutController: WorkoutController
    private lateinit var viewModel: NewWorkoutViewModel
    private var workouts = ArrayList<Workout>()
    private var exercises = listOf<ExerciseEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            R.style.FullScreenDialog
        )

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
        CoroutineScope(Dispatchers.IO).launch {
            exercises = viewModel.getExercises()
        }
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

        val date = viewModel.simpleDateFormat.parse(newWorkoutDateText.text.toString()).toInstant()
        val workoutSession = WorkoutSession(newWorkoutNameText.text.toString(), date, workouts)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.saveWorkoutSession(workoutSession)
        }
        if (args.workoutSessionID != null) {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.deleteWorkoutSession(args.workoutSessionID!!)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFields()
        initDatePicker()

        newWorkoutToolbar.apply {
            title = resources.getString(if (args.workoutSessionID == null) R.string.new_workout else R.string.edit_workout)
            setNavigationOnClickListener { dismiss() }
        }

        addWorkoutBtn.setOnClickListener {
            this.addEmptyWorkout()
            workoutController.setData(workouts)
        }

        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.dropdown_menu_popup_item,
            exercises.map { it.name })

        workoutController.setAdapter(adapter)
        recyclerView.setController(workoutController)
        workoutController.setData(workouts)
    }
    // //////////////////////////////////////////////////////////////
    // Helpers
    // //////////////////////////////////////////////////////////////

    private fun addEmptyWorkout() {
        val workout = Workout(
            exercise = Exercise("", 0.0, ""),
            sets = ArrayList()
        )
        workouts.add(0, workout)
    }

    // //////////////////////////////////////////////////////////////
    // Init
    // //////////////////////////////////////////////////////////////
    private fun initFields() {
        if (args.workoutSessionID == null) {
            return
        }
        if (args.workoutSessionName != null) {
            newWorkoutNameText.setText(args.workoutSessionName)
        }
        if (args.workoutSessionDate != 0.toLong()) {
            newWorkoutDateText.setText(
                viewModel
                    .simpleDateFormat
                    .format(Date.from(Instant.ofEpochSecond(args.workoutSessionDate))))
        }
        CoroutineScope(Dispatchers.IO).launch {
            workouts = viewModel.getWorkouts(args.workoutSessionID!!)
        }
    }

    private fun initDatePicker() {

        if (newWorkoutDateText.text.isNullOrEmpty()) {
            newWorkoutDateText.setText(viewModel.simpleDateFormat.format(Date.from(Instant.now())))
        }

        val myCalendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener {
                _, year, month, day -> myCalendar.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
            newWorkoutDateText.setText(viewModel.simpleDateFormat.format(myCalendar.time))
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
        workouts[index] = workouts[index].addSet(WorkoutSet(weight = 0.0, reps = 0))
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

        val exercise = exercises.find { it.name == exercise }
        if (exercise == null) {
            Log.e(this.javaClass.canonicalName, "Exercise not found")
            return
        }
        workouts[workoutIndex] = workouts[workoutIndex]
            .updateExercise(Exercise(exercise.name, exercise.personalRecord, exercise.muscleGroup))
        workoutController.setData(workouts)
    }
}