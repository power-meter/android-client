package io.mochahub.powermeter.workoutsession

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import io.mochahub.powermeter.R
import kotlinx.android.synthetic.main.dialog_new_workout_dialog.newWorkoutDateText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

        initDateField()
    }

    private fun initDateField() {
        val myCalendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener {
                datePicker, year, month, day -> myCalendar.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
            // TODO: Make a const
            val myFormat = "mm/dd/yy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
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