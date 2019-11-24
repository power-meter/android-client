package io.mochahub.powermeter.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import io.mochahub.powermeter.R
import kotlinx.android.synthetic.main.fragment_edit_exercise.*

class EditExerciseFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_exercise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exerciseNameText.doOnTextChanged { text, start, count, after ->
            saveBtn.isEnabled = text!!.isNotEmpty()
        }
        saveBtn.setOnClickListener {
            dismiss()
        }
    }
}
