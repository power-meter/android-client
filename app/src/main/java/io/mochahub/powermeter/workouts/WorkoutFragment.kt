package io.mochahub.powermeter.workouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.mochahub.powermeter.R
import io.mochahub.powermeter.models.WorkoutSession
import kotlinx.android.synthetic.main.fragment_workout.*

class WorkoutFragment : Fragment() {

    private val viewModel: WorkoutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val workoutAdapter = WorkoutAdapter(viewModel.workoutSessions.value ?: listOf()) { clicked : WorkoutSession -> onWorkoutSessionClicked(clicked) }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = workoutAdapter
        }

        viewModel.workoutSessions.observe(viewLifecycleOwner, Observer {
            workoutAdapter.setData(it ?: listOf())
        })

        // TODO: Show a dialog fragment with editor where we can CRUD new session
        addSessionButton.setOnClickListener {
            viewModel.addWorkout()
        }
    }

    // TODO: Show a dialog fragment with editor where we can CRUD this session
    private fun onWorkoutSessionClicked(workoutSession: WorkoutSession) {
        Toast.makeText(requireContext(), "Clicked: ${workoutSession.date}", Toast.LENGTH_LONG).show()
    }
}