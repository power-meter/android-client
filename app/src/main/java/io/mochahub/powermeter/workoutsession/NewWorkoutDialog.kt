package io.mochahub.powermeter.workoutsession

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import io.mochahub.powermeter.R
import kotlinx.android.synthetic.main.dialog_new_workout_dialog.newWorkoutDateText
import kotlinx.android.synthetic.main.dialog_new_workout_dialog.newWorkoutToolbar
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
        return inflater.inflate(R.layout.dialog_new_workout_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFields()
        initDatePicker()

        newWorkoutToolbar.apply {
            title = resources.getString(R.string.new_workout)
            setNavigationOnClickListener { dismiss() }
            inflateMenu(R.menu.menu_save)
            setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.action_save -> {
                        // TODO: No error checking yet, so it'll crash if you don't give an actual double lol
//                        newExerciseSharedViewModel.saveNewExercise(
//                            Exercise(
//                                newExerciseNameText.text.toString(),
//                                newExercisePRText.text.toString().toDouble(),
//                                newExerciseGroupText.text.toString()
//                            )
//                        )
                    }
                }
                dismiss()
                true
            }
        }
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