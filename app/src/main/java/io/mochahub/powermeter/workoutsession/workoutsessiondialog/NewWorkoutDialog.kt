package io.mochahub.powermeter.workoutsession.workoutsessiondialog

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.airbnb.epoxy.EpoxyTouchHelper
import io.mochahub.powermeter.R
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.data.ExerciseEntity
import io.mochahub.powermeter.models.Exercise
import io.mochahub.powermeter.models.Workout
import io.mochahub.powermeter.models.WorkoutSession
import io.mochahub.powermeter.models.WorkoutSet
import io.mochahub.powermeter.models.addSet
import io.mochahub.powermeter.models.removeSet
import io.mochahub.powermeter.models.setReps
import io.mochahub.powermeter.models.setWeight
import io.mochahub.powermeter.models.updateExercise
import kotlinx.android.synthetic.main.dialog_new_workout.addWorkoutBtn
import kotlinx.android.synthetic.main.dialog_new_workout.newWorkoutDateText
import kotlinx.android.synthetic.main.dialog_new_workout.newWorkoutToolbar
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
    private val viewModel by lazy {
        ViewModelProviders.of(
            this,
            NewWorkoutViewModel(AppDatabase(requireContext()))
        )[NewWorkoutViewModel::class.java]
    }
    private var exercises = listOf<ExerciseEntity>()

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
        if (shouldSave) {
            saveWorkoutSession()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        viewModel.isReady.observe(viewLifecycleOwner, Observer {
            if (it) {
                onViewCreatedReady()
                viewModel.isReady.postValue(false)
            }
        })
    }

    // //////////////////////////////////////////////////////////////
    // Helpers
    // //////////////////////////////////////////////////////////////
    private fun onViewCreatedReady() {
        recyclerView.setController(workoutController)
        workoutController.setData(viewModel.workouts)

        EpoxyTouchHelper.initSwiping(recyclerView)
            .left()
            .withTarget(WorkoutRowSetModel::class.java)
            .andCallbacks(
                WorkoutRowSetSwipeCallBack(
                    requireContext().resources
                ) { workoutSet ->
                    for (i in 0 until viewModel.workouts.size) {
                        for (k in 0 until viewModel.workouts[i].sets.size) {
                            if (viewModel.workouts[i].sets[k].id == workoutSet.id) {
                                viewModel.workouts[i] =
                                    viewModel.workouts[i].removeSet(k)
                                workoutController.setData(viewModel.workouts)
                                break
                            }
                        }
                    }
                })

        newWorkoutToolbar.apply {
            title =
                resources.getString(if (args.workoutSessionID == null) R.string.new_workout else R.string.edit_workout)
            inflateMenu(R.menu.menu_cancel)
            setNavigationOnClickListener { dismiss() }
            setOnMenuItemClickListener {
                when (it?.itemId) {
                    R.id.action_cancel -> {
                        shouldSave = false
                        dismiss()
                    }
                }
                true
            }
        }

        addWorkoutBtn.setOnClickListener {
            this.addEmptyWorkout()
            workoutController.setData(viewModel.workouts)
        }
    }

    private fun addEmptyWorkout() {
        val sets = ArrayList<WorkoutSet>()
        sets.add(
            WorkoutSet(
                weight = 0.0,
                reps = 0
            )
        )
        val workout = Workout(
            exercise = Exercise("", 0.0, ""),
            sets = sets
        )
        viewModel.workouts.add(workout)
    }

    private fun saveWorkoutSession() {
        val date = viewModel.simpleDateFormat.parse(newWorkoutDateText.text.toString()).toInstant()
        val workoutSession =
            WorkoutSession(date, viewModel.workouts)
        viewModel.saveWorkoutSession(workoutSession, args.workoutSessionID)
    }

    // //////////////////////////////////////////////////////////////
    // Init
    // //////////////////////////////////////////////////////////////
    private fun initialize() {
        initDatePicker()
        viewModel.getExercises().observe(viewLifecycleOwner, Observer { exerciseList ->
            exercises = exerciseList
            workoutController =
                WorkoutController(
                    requireContext(),
                    exerciseList.map { it.name },
                    this
                )
            initFields()
        })
    }

    private fun initDatePicker() {

        if (newWorkoutDateText.text.isNullOrEmpty()) {
            newWorkoutDateText.setText(viewModel.simpleDateFormat.format(Date.from(Instant.now())))
        }

        val myCalendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            myCalendar.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, day)
                newWorkoutDateText.setText(viewModel.simpleDateFormat.format(myCalendar.time))
            }
        }

        newWorkoutDateText.setOnClickListener {
            DatePickerDialog(
                requireContext(), dateSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun initFields() {
        if (args.workoutSessionID == null) {
            if (viewModel.workouts.isEmpty()) {
                this.addEmptyWorkout()
                workoutController.setData(viewModel.workouts)
            }
            viewModel.isReady.postValue(true)
        } else {
            if (args.workoutSessionDate != 0.toLong()) {
                newWorkoutDateText.setText(
                    viewModel
                        .simpleDateFormat
                        .format(Date.from(Instant.ofEpochSecond(args.workoutSessionDate)))
                )
            }
            // TODO: Remove this. Requires implementing room relations
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.workouts = viewModel.getWorkouts(args.workoutSessionID!!)
                workoutController.setData(viewModel.workouts)
            }.invokeOnCompletion {
                viewModel.isReady.postValue(true)
            }
        }
    }

    // //////////////////////////////////////////////////////////////
    // Interface methods for workout controller
    // //////////////////////////////////////////////////////////////
    // Need to use a loop method because it is more reliable then indexing
    // If we use index's there is a bug when deleting sets/ workouts
    // that causes out of bounds exceptions
    override fun onAddSetClicked(workout: Workout) {
        for (i in 0 until viewModel.workouts.size) {
            if (viewModel.workouts[i].id == workout.id) {
                viewModel.workouts[i] =
                    viewModel.workouts[i].addSet(WorkoutSet(weight = 0.0, reps = 0))
                workoutController.setData(viewModel.workouts)
            }
        }
    }

    override fun onRepTextChanged(workout: Workout, workoutSet: WorkoutSet, value: Int) {
        for (i in 0 until viewModel.workouts.size) {
            if (viewModel.workouts[i].id == workout.id) {
                for (k in 0 until viewModel.workouts[i].sets.size) {
                    if (viewModel.workouts[i].sets[k].id == workoutSet.id) {
                        viewModel.workouts[i].sets[k] = viewModel.workouts[i].sets[k].setReps(value)
                        workoutController.setData(viewModel.workouts)
                    }
                }
            }
        }
    }

    override fun onWeightTextChanged(workout: Workout, workoutSet: WorkoutSet, value: Double) {
        for (i in 0 until viewModel.workouts.size) {
            if (viewModel.workouts[i].id == workout.id) {
                for (k in 0 until viewModel.workouts[i].sets.size) {
                    if (viewModel.workouts[i].sets[k].id == workoutSet.id) {
                        viewModel.workouts[i].sets[k] =
                            viewModel.workouts[i].sets[k].setWeight(value)
                        workoutController.setData(viewModel.workouts)
                    }
                }
            }
        }
    }

    override fun onExerciseSelected(workout: Workout, exercise: String) {

        val foundExercise = exercises.find { it.name == exercise }

        for (i in 0 until viewModel.workouts.size) {
            if (viewModel.workouts[i].id == workout.id) {
                viewModel.workouts[i] = viewModel.workouts[i]
                    .updateExercise(
                        Exercise(
                            foundExercise!!.name,
                            foundExercise.personalRecord,
                            foundExercise.muscleGroup
                        )
                    )
                workoutController.setData(viewModel.workouts)
            }
        }
    }
}
