package io.mochahub.powermeter.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import io.mochahub.powermeter.R
import io.mochahub.powermeter.data.AppDatabase
import kotlinx.android.synthetic.main.dialog_new_exercise.newExerciseGroupText
import kotlinx.android.synthetic.main.dialog_new_exercise.newExerciseNameText
import kotlinx.android.synthetic.main.dialog_new_exercise.newExercisePRText
import kotlinx.android.synthetic.main.dialog_new_exercise.newExerciseToolbar

class ExerciseDialog : DialogFragment() {

    private val args: ExerciseDialogArgs by navArgs()

    private val viewModel by lazy {
        requireActivity().run {
            ViewModelProvider(
                this,
                ExerciseDialogViewModel.ExerciseDialogViewModelFactory(
                    AppDatabase(requireContext()).exerciseDao(), args
                )
            ).get(ExerciseDialogViewModel::class.java)
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
        val exercise = viewModel.getExercise()

        if (exercise.name.isNotBlank()) {
            newExerciseNameText.setText(args.exerciseName)
        }
        if (exercise.personalRecord != 0.0) {
            newExercisePRText.setText(args.exercisePR.toString())
        }
        if (exercise.muscleGroup.isNotBlank()) {
            newExerciseGroupText.setText(args.muscleGroup)
        }

        newExerciseToolbar.apply {
            title =
                if (args.exerciseName.isNotBlank()) resources.getString(R.string.edit_exercise) else resources.getString(
                    R.string.new_exercise
                )
            setNavigationOnClickListener { dismiss() }
            inflateMenu(R.menu.menu_save)
            setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.action_save -> {
                        viewModel.upsertExercise(
                            newExerciseNameText.text.toString(),
                            newExerciseGroupText.text.toString(),
                            newExercisePRText.text.toString().toDoubleOrZero())
                    }
                }
                dismiss()
                true
            }
        }
    }
}

fun String.toDoubleOrZero(): Double = if (this.toDoubleOrNull() == null) 0.0 else this.toDouble()
