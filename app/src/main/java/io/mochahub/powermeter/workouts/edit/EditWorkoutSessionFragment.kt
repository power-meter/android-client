package io.mochahub.powermeter.workouts.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import io.mochahub.powermeter.R
import io.mochahub.powermeter.workouts.edit.adapters.EditWorkoutExerciseAdapter
import kotlinx.android.synthetic.main.fragment_workout_session_edit.*

class EditWorkoutSessionFragment : Fragment() {

    private val sessionViewModel: EditWorkoutSessionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workout_session_edit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val workoutAdapter =
            EditWorkoutExerciseAdapter(
                sessionViewModel.workouts.value ?: listOf()
            )
        exercise_recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = workoutAdapter
        }
    }
}