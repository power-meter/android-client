package io.mochahub.powermeter.workoutsession.workoutsessiondialog

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
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
import kotlinx.android.synthetic.main.dialog_new_workout.newWorkoutNameText
import kotlinx.android.synthetic.main.dialog_new_workout.newWorkoutToolbar
import kotlinx.android.synthetic.main.fragment_exercise.recyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Calendar
import java.util.Date
import kotlin.math.absoluteValue

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

        EpoxyTouchHelper.initSwiping(recyclerView)
            .left()
            .withTarget(WorkoutRowSetModel::class.java)
            .andCallbacks(WorkoutRowSetSwipeCallBack(requireContext().resources) { workoutIndex, workoutSetIndex ->
                viewModel.workouts[workoutIndex] =
                    viewModel.workouts[workoutIndex].removeSet(workoutSetIndex)
                workoutController.setData(viewModel.workouts)
            })

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
        var error: String?
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
    // Need to use a loop method because it is more reliable then indexing
    // If we use index's there is a bug when deleting sets/ workouts
    // that causes out of bounds exceptions
    override fun onAddSetClicked(workout: Workout) {
        for (i in 0 until viewModel.workouts.size) {
            if (viewModel.workouts[i].id == workout.id) {
                viewModel.workouts[i] = viewModel.workouts[i].addSet(0, WorkoutSet(weight = 0.0, reps = 0))
                workoutController.setData(viewModel.workouts)
            }
        }
    }

    override fun onRepFocusChanged(workout: Workout, workoutSet: WorkoutSet, value: Int) {
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

    override fun onWeightFocusChanged(workout: Workout, workoutSet: WorkoutSet, value: Double) {
        for (i in 0 until viewModel.workouts.size) {
            if (viewModel.workouts[i].id == workout.id) {
                for (k in 0 until viewModel.workouts[i].sets.size) {
                    if (viewModel.workouts[i].sets[k].id == workoutSet.id) {
                        viewModel.workouts[i].sets[k] = viewModel.workouts[i].sets[k].setWeight(value)
                        workoutController.setData(viewModel.workouts)
                    }
                }
            }
        }
    }

    override fun onExerciseSelected(workout: Workout, exercise: String) {

        val foundExercise = exercises.find { it.name == exercise }
        if (foundExercise == null) {
            Log.e(this.javaClass.canonicalName, "Exercise not found")
            return
        }

        for (i in 0 until viewModel.workouts.size) {
            if (viewModel.workouts[i].id == workout.id) {
                viewModel.workouts[i] = viewModel.workouts[i]
                    .updateExercise(Exercise(foundExercise.name, foundExercise.personalRecord, foundExercise.muscleGroup))
                workoutController.setData(viewModel.workouts)
            }
        }
    }
}

private class WorkoutRowSetSwipeCallBack(
    private val resources: Resources,
    private val onLeftSwipe: (Int, Int) -> Unit
) : EpoxyTouchHelper.SwipeCallbacks<WorkoutRowSetModel>() {

    private val paint = Paint()

    init {
        paint.setARGB(255, 255, 0, 0)
    }

    private fun getDeleteIcon(): Bitmap {
        val vectorDrawable = (resources.getDrawable(R.drawable.ic_delete_white_24dp, null))
        val icon = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(icon)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return icon
    }

    override fun onSwipeCompleted(
        model: WorkoutRowSetModel?,
        itemView: View?,
        position: Int,
        direction: Int
    ) {
        if (direction == (ItemTouchHelper.LEFT) && model != null) {
            onLeftSwipe(model.workoutIndex, model.workoutSetIndex)
        }
    }

    override fun onSwipeProgressChanged(
        model: WorkoutRowSetModel?,
        itemView: View?,
        swipeProgress: Float,
        canvas: Canvas?
    ) {
        super.onSwipeProgressChanged(model, itemView, swipeProgress, canvas)
        if (itemView != null && canvas != null && model != null) {
            val icon = getDeleteIcon()

            canvas.drawRect(
                (itemView.width.toFloat() - itemView.width.toFloat()*(swipeProgress.absoluteValue)),
                itemView.top.toFloat(),
                itemView.width.toFloat(), itemView.bottom.toFloat(), paint)

            val width = (itemView.bottom.toFloat() - itemView.top.toFloat()) / 3
            val iconDest = RectF(
                itemView.right.toFloat() - 2 * width,
                itemView.top.toFloat() + width,
                itemView.right.toFloat() - width,
                itemView.bottom.toFloat() - width
            )
            canvas.drawBitmap(icon, null, iconDest, paint)
        }
    }
}
