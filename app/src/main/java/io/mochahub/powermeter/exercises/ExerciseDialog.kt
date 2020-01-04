package io.mochahub.powermeter.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import io.mochahub.powermeter.R
import io.mochahub.powermeter.data.ExerciseEntity
import kotlinx.android.synthetic.main.dialog_new_exercise.newExerciseGroupText
import kotlinx.android.synthetic.main.dialog_new_exercise.newExerciseNameText
import kotlinx.android.synthetic.main.dialog_new_exercise.newExercisePRText
import kotlinx.android.synthetic.main.dialog_new_exercise.newExerciseToolbar

class ExerciseDialog : DialogFragment() {

    private val args: ExerciseDialogArgs by navArgs()

    private val newExerciseSharedViewModel by lazy {
        requireActivity().run {
            ViewModelProviders.of(this)[ExerciseSharedViewModel::class.java]
        }
    }

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

        args.exerciseName?.let { newExerciseNameText.setText(it) }
        args.exercisePR.let { newExercisePRText.setText(it.toString()) }
        args.muscleGroup?.let { newExerciseGroupText.setText(it) }

        newExerciseToolbar.apply {
            title = if (args.shouldEdit) resources.getString(R.string.edit_exercise) else resources.getString(R.string.new_exercise)
            setNavigationOnClickListener { dismiss() }
            inflateMenu(R.menu.menu_save)
            setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.action_save -> {
                        saveExercise()
                    }
                }
                dismiss()
                true
            }
        }
    }

    private fun saveExercise() {
        if (args.shouldEdit) {
            newExerciseSharedViewModel.saveEditExercise(
                ExerciseEntity(
                    id = args.exerciseId,
                    name = newExerciseNameText.text.toString(),
                    personalRecord = newExercisePRText.toDoubleOrZero(),
                    muscleGroup = newExerciseGroupText.text.toString()
                )
            )
        } else {
            newExerciseSharedViewModel.saveNewExercise(
                ExerciseEntity(
                    name = newExerciseNameText.text.toString(),
                    personalRecord = newExercisePRText.toDoubleOrZero(),
                    muscleGroup = newExerciseGroupText.text.toString()
                )
            )
        }
    }
}

/**
 * Convenience extension used for TextInputEditText for personal record to default to zero if there
 * is an empty string, which allows us to avoid a crash where string was empty!
 */
fun TextInputEditText.toDoubleOrZero(): Double {
    val text = this.text.toString()
    return if (text.isEmpty()) {
        0.0
    } else {
        text.toDouble()
    }
}
