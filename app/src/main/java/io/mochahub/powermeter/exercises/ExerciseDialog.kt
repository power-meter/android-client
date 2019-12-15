package io.mochahub.powermeter.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import io.mochahub.powermeter.R
import io.mochahub.powermeter.data.Exercise
import kotlinx.android.synthetic.main.dialog_new_exercise.*

class ExerciseDialog : DialogFragment() {

    private val args: ExerciseDialogArgs by navArgs()

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
            ViewModelProviders.of(this)[ExerciseSharedViewModel::class.java]
        }

        args.exerciseName?.let { newExerciseNameText.setText(it) }
        args.exercisePR.let { newExercisePRText.setText(it.toString()) }
        args.muscleGroup?.let { newExerciseGroupText.setText(it) }

        newExerciseToolbar.apply {
            title = if (args.shouldEdit) resources.getString(R.string.edit_exercise) else resources.getString(R.string.new_exercise)
            setNavigationOnClickListener { dismiss() }
            inflateMenu(R.menu.menu_new_exercise)
            setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.action_save -> {
                        // TODO: No error checking yet, so it'll crash if you don't give an actual double lol
                        if (args.shouldEdit) {
                            newExerciseSharedViewModel.saveEditExercise(
                                Exercise(
                                    id = args.exerciseId,
                                    name = newExerciseNameText.text.toString(),
                                    personalRecord = newExercisePRText.text.toString().toDouble(),
                                    muscleGroup = newExerciseGroupText.text.toString()
                                )
                            )
                        } else {
                            newExerciseSharedViewModel.saveNewExercise(
                                Exercise(
                                    name = newExerciseNameText.text.toString(),
                                    personalRecord = newExercisePRText.text.toString().toDouble(),
                                    muscleGroup = newExerciseGroupText.text.toString()
                                )
                            )
                        }
                    }
                }
                dismiss()
                true
            }
        }
    }
}
