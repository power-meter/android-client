package io.mochahub.powermeter.workoutsession

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.mochahub.powermeter.R
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.data.WorkoutSessionEntity
import io.mochahub.powermeter.shared.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.fragment_workout_session.*

class WorkoutSessionFragment : Fragment() {

    private val navController by lazy { this.findNavController() }
    private val sessionViewModel by lazy { WorkoutSessionViewModel(db = AppDatabase(requireContext())) }
    private val itemTouchHelper by lazy { ItemTouchHelper(swipeHandler) }
    private val swipeHandler by lazy {
        object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                val deletedWorkoutSession: WorkoutSessionEntity = sessionViewModel.removeWorkoutSession(position)
                Snackbar.make(
                    viewHolder.itemView,
                    getString(R.string.exercise_deleted),
                    Snackbar.LENGTH_LONG
                )
                    // TODO: UNDO. This is more tricky because we need to undo the cascade delete
                    .apply {
                        setAction(getString(R.string.undo)) { Log.d(this.javaClass.toString(), "TODO: UNDO DELETED WORKOUTSESSION") }
                        setActionTextColor(Color.YELLOW)
                        show()
                    }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workout_session, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = resources.getString(R.string.workout_session_screen_label)

        val workoutAdapter = WorkoutSessionAdapter(
            sessionViewModel.workoutSessions.value ?: listOf()) {
            onWorkoutSessionClicked(it) }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = workoutAdapter
        }
        itemTouchHelper.attachToRecyclerView(recyclerView)

        sessionViewModel.workoutSessions.observe(viewLifecycleOwner, Observer {
            workoutAdapter.setData(it ?: listOf())
        })

        addSessionButton.setOnClickListener {
            val action = WorkoutSessionFragmentDirections
                .actionDestinationWorkoutSessionScreenToNewWorkoutDialog(
                    workoutSessionID = null,
                    workoutSessionName = null,
                    workoutSessionDate = 0
                )
            navController.navigate(action)
        }
    }

    private fun onWorkoutSessionClicked(workoutSession: WorkoutSessionEntity) {
        val action = WorkoutSessionFragmentDirections
            .actionDestinationWorkoutSessionScreenToNewWorkoutDialog(
                workoutSessionID = workoutSession.id,
                workoutSessionName = workoutSession.name,
                workoutSessionDate = workoutSession.date
            )
        navController.navigate(action)
    }
}