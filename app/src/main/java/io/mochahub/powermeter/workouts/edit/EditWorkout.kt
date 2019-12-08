package io.mochahub.powermeter.workouts.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import io.mochahub.powermeter.R
import kotlinx.android.synthetic.main.fragment_workout_edit.*

class EditWorkout : Fragment() {

    companion object {
        fun newInstance() = EditWorkout()
    }

    private val viewModel: EditWorkoutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workout_edit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val workoutAdapter = EditWorkoutExerciseAdapter(viewModel.workouts.value ?: listOf())
        exercise_recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = workoutAdapter
        }
    }
}