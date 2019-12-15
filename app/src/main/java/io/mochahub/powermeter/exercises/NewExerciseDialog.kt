package io.mochahub.powermeter.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import io.mochahub.powermeter.R
import io.mochahub.powermeter.models.Exercise
import kotlinx.android.synthetic.main.dialog_new_exercise.*

class NewExerciseDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
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
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_new_exercise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newExerciseSharedViewModel = requireActivity().run {
            ViewModelProviders.of(this)[NewExerciseSharedViewModel::class.java]
        }

        newExerciseToolbar.apply {
            title = resources.getString(R.string.new_exercise)
            setNavigationOnClickListener { dismiss() }
            inflateMenu(R.menu.menu_new_exercise)
            setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.action_save -> {
                        // TODO: No error checking yet, so it'll crash if you don't give an actual double lol
                        newExerciseSharedViewModel.saveNewExercise(
                            Exercise(
                                newExerciseNameText.text.toString(),
                                newExercisePRText.text.toString().toDouble(),
                                newExerciseGroupText.text.toString()
                            )
                        )
                    }
                }
                dismiss()
                true
            }
        }
    }
}
