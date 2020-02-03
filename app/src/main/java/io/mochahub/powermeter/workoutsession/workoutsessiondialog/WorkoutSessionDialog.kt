package io.mochahub.powermeter.workoutsession.workoutsessiondialog

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.airbnb.epoxy.EpoxyTouchHelper
import io.mochahub.powermeter.R
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.data.exercise.ExerciseEntity
import io.mochahub.powermeter.data.workout.toModel
import io.mochahub.powermeter.models.Exercise
import io.mochahub.powermeter.models.Workout
import io.mochahub.powermeter.models.WorkoutSet
import io.mochahub.powermeter.models.addSet
import io.mochahub.powermeter.models.removeSet
import io.mochahub.powermeter.models.setDate
import io.mochahub.powermeter.models.setReps
import io.mochahub.powermeter.models.setVisibility
import io.mochahub.powermeter.models.setWeight
import io.mochahub.powermeter.models.updateExercise
import kotlinx.android.synthetic.main.dialog_new_workout_session.addWorkoutBtn
import kotlinx.android.synthetic.main.dialog_new_workout_session.newWorkoutToolbar
import kotlinx.android.synthetic.main.fragment_exercise.recyclerView
import java.util.Calendar

class WorkoutSessionDialog : WorkoutController.AdapterCallbacks, DialogFragment() {

    private val args: WorkoutSessionDialogArgs by navArgs()
    private var shouldSave = true
    private lateinit var workoutController: WorkoutController
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            WorkoutSessionDialogViewModel(AppDatabase(requireContext()))
        ).get(WorkoutSessionDialogViewModel::class.java)
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
        return inflater.inflate(R.layout.dialog_new_workout_session, container, false)
    }

    override fun onStop() {
        super.onStop()

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
        workoutController.setData(viewModel.workoutSession)

        EpoxyTouchHelper.initSwiping(recyclerView)
            .left()
            .withTarget(WorkoutRowSetModel::class.java)
            .andCallbacks(
                WorkoutRowSetSwipeCallBack(
                    requireContext().resources
                ) { workoutSet ->
                    for (i in viewModel.workoutSession.workouts.indices) {
                        for (k in 0 until viewModel.workoutSession.workouts[i].sets.size) {
                            if (viewModel.workoutSession.workouts[i].sets[k].id == workoutSet.id) {
                                (viewModel.workoutSession.workouts as ArrayList)[i] =
                                    viewModel.workoutSession.workouts[i].removeSet(k)
                                workoutController.setData(viewModel.workoutSession)
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
        (viewModel.workoutSession.workouts as ArrayList).add(workout)
        workoutController.setData(viewModel.workoutSession)
    }

    private fun saveWorkoutSession() {
        viewModel.saveWorkoutSession(args.workoutSessionID)
    }

    // //////////////////////////////////////////////////////////////
    // Init
    // //////////////////////////////////////////////////////////////
    private fun initialize() {
        val myCalendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            myCalendar.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, day)
                viewModel.workoutSession = viewModel.workoutSession.setDate(myCalendar.toInstant())
                workoutController.setData(viewModel.workoutSession)
            }
        }

        val datePickerDialog = DatePickerDialog(
            requireContext(), dateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        )

        viewModel.getExercises().observe(viewLifecycleOwner, Observer { exerciseList ->
            exercises = exerciseList
            workoutController =
                WorkoutController(
                    requireContext(),
                    exerciseList.map { it.name },
                    datePickerDialog,
                    this
                )
            initFields()
        })
    }

    private fun initFields() {
        if (args.workoutSessionID == null) {
            if (viewModel.workoutSession.workouts.isEmpty()) {
                this.addEmptyWorkout()
            }
            viewModel.isReady.postValue(true)
        } else {
            if (args.workoutSessionDate != 0.toLong()) {
                viewModel.workoutSession = viewModel.workoutSession.setDate(args.workoutSessionDate)
            }
            viewModel.getWorkouts(args.workoutSessionID!!).observe(viewLifecycleOwner, Observer {
                it.forEach { workoutRelation ->
                    val workoutModel = if (workoutRelation.workoutSets.isEmpty())
                        workoutRelation.toModel().addSet(WorkoutSet(reps = 0, weight = 0.0))
                        else workoutRelation.toModel()
                    (viewModel.workoutSession.workouts as ArrayList).add(workoutModel)
                }
                if (viewModel.workoutSession.workouts.isEmpty()) {
                    this.addEmptyWorkout()
                }
                workoutController.setData(viewModel.workoutSession)
                viewModel.isReady.postValue(true)
            })
        }
    }

    // //////////////////////////////////////////////////////////////
    // Interface methods for workout controller
    // //////////////////////////////////////////////////////////////
    // Need to use a loop method because it is more reliable then indexing
    // If we use index's there is a bug when deleting sets/ workouts
    // that causes out of bounds exceptions
    // TODO: make workouts a hashmap and change model so that workoutsets are a hashmap.
    //  The keys will be their ids.
    override fun addEmptyWorkoutSet(workout: Workout) {
        for (i in viewModel.workoutSession.workouts.indices) {
            if (viewModel.workoutSession.workouts[i].id == workout.id) {
                (viewModel.workoutSession.workouts as ArrayList)[i] =
                    viewModel.workoutSession.workouts[i].addSet(WorkoutSet(weight = 0.0, reps = 0))
                workoutController.setData(viewModel.workoutSession)
            }
        }
    }

    override fun toggleWorkoutSetVisibility(visible: Boolean, workout: Workout) {
        for (i in viewModel.workoutSession.workouts.indices) {
            if (viewModel.workoutSession.workouts[i].id == workout.id) {
                (viewModel.workoutSession.workouts as ArrayList)[i] =
                    viewModel.workoutSession.workouts[i].setVisibility(visible)
                workoutController.setData(viewModel.workoutSession)
            }
        }
    }

    override fun onRepTextChanged(workout: Workout, workoutSet: WorkoutSet, value: Int) {
        for (i in viewModel.workoutSession.workouts.indices) {
            if (viewModel.workoutSession.workouts[i].id == workout.id) {
                for (k in 0 until viewModel.workoutSession.workouts[i].sets.size) {
                    if (viewModel.workoutSession.workouts[i].sets[k].id == workoutSet.id) {
                        viewModel.workoutSession.workouts[i].sets[k] =
                            viewModel.workoutSession.workouts[i].sets[k].setReps(value)
                        workoutController.setData(viewModel.workoutSession)
                    }
                }
            }
        }
    }

    override fun onWeightTextChanged(workout: Workout, workoutSet: WorkoutSet, value: Double) {
        for (i in viewModel.workoutSession.workouts.indices) {
            if (viewModel.workoutSession.workouts[i].id == workout.id) {
                for (k in 0 until viewModel.workoutSession.workouts[i].sets.size) {
                    if (viewModel.workoutSession.workouts[i].sets[k].id == workoutSet.id) {
                        viewModel.workoutSession.workouts[i].sets[k] =
                            viewModel.workoutSession.workouts[i].sets[k].setWeight(value)
                        workoutController.setData(viewModel.workoutSession)
                    }
                }
            }
        }
    }

    override fun onExerciseSelected(workout: Workout, exercise: String) {

        val foundExercise = exercises.find { it.name == exercise }

        for (i in viewModel.workoutSession.workouts.indices) {
            if (viewModel.workoutSession.workouts[i].id == workout.id) {
                (viewModel.workoutSession.workouts as ArrayList)[i] =
                    viewModel.workoutSession.workouts[i]
                        .updateExercise(
                            Exercise(
                                foundExercise!!.name,
                                foundExercise.personalRecord,
                                foundExercise.muscleGroup
                            )
                        )
                workoutController.setData(viewModel.workoutSession)
            }
        }
    }
}
