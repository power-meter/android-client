package io.mochahub.powermeter.workoutsession.workoutsessiondialog

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private var shouldSave = true
    private lateinit var workoutController: WorkoutController
    private lateinit var viewModel: NewWorkoutViewModel
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
        CoroutineScope(Dispatchers.IO).launch {
            exercises = viewModel.getExercises()
        }.invokeOnCompletion {
            workoutController =
                WorkoutController(
                    requireContext(),
                    exercises.map { it.name },
                    this
                )
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        workoutController.setData(viewModel.workouts)
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

        if (shouldSave) {
            saveWorkoutSession()
        } else {
            shouldSave = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.setController(workoutController)
        workoutController.setData(viewModel.workouts)

        newWorkoutToolbar.apply {
            title = resources.getString(if (args.workoutSessionID == null) R.string.new_workout else R.string.edit_workout)
            inflateMenu(R.menu.menu_cancel)
            setNavigationOnClickListener { dismiss() }
            setOnMenuItemClickListener {
                when (it?.itemId) {
                    R.id.action_cancel -> {
                        shouldSave = false
                        dismiss() }
                }
                true
            }
        }
        addWorkoutBtn.setOnClickListener {
            this.addEmptyWorkout()
            workoutController.setData(viewModel.workouts)
        }

        initFields()
        initDatePicker()
    }
    // //////////////////////////////////////////////////////////////
    // Helpers
    // //////////////////////////////////////////////////////////////

    private fun addEmptyWorkout() {
        val workout = Workout(
            exercise = Exercise("", 0.0, ""),
            sets = ArrayList()
        )
        viewModel.workouts.add(0, workout)
    }

    private fun saveWorkoutSession() {
        val date = viewModel.simpleDateFormat.parse(newWorkoutDateText.text.toString()).toInstant()
        val workoutSession = WorkoutSession(newWorkoutNameText.text.toString(), date, viewModel.workouts)
        var error: String? = null
        CoroutineScope(Dispatchers.IO).launch {
            error = viewModel.saveWorkoutSession(workoutSession)
            if (error == null && args.workoutSessionID != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.deleteWorkoutSession(args.workoutSessionID!!)
                }
            }
        }
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
            viewModel.workouts = viewModel.getWorkouts(args.workoutSessionID!!)
            workoutController.setData(viewModel.workouts)
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
        viewModel.workouts[index] = viewModel.workouts[index].addSet(0, WorkoutSet(weight = 0.0, reps = 0))
        workoutController.setData(viewModel.workouts)
    }

    override fun onRepFocusChanged(workoutIndex: Int, setIndex: Int, value: Int) {
        viewModel.workouts[workoutIndex].sets[setIndex] = viewModel.workouts[workoutIndex].sets[setIndex].setReps(value)
        workoutController.setData(viewModel.workouts)
    }

    override fun onWeightFocusChanged(workoutIndex: Int, setIndex: Int, value: Double) {
        viewModel.workouts[workoutIndex].sets[setIndex] = viewModel.workouts[workoutIndex].sets[setIndex].setWeight(value)
        workoutController.setData(viewModel.workouts)
    }

    override fun onExerciseSelected(workoutIndex: Int, exercise: String) {

        val foundExercise = exercises.find { it.name == exercise }
        if (foundExercise == null) {
            Log.e(this.javaClass.canonicalName, "Exercise not found")
            return
        }
        viewModel.workouts[workoutIndex] = viewModel.workouts[workoutIndex]
            .updateExercise(Exercise(foundExercise.name, foundExercise.personalRecord, foundExercise.muscleGroup))
        workoutController.setData(viewModel.workouts)
    }
}