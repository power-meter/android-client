package io.mochahub.powermeter.workouts

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.mochahub.powermeter.R
import io.mochahub.powermeter.models.WorkoutSession
import io.mochahub.powermeter.shared.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.fragment_workout.*

private const val DELETED_WORKOUT_MSSG = "Workout session deleted!"
private const val UNDO_WORKOUT_MSSG = "UNDO"

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
        activity?.title = resources.getString(R.string.workout_session_screen_label)

        val workoutAdapter = WorkoutAdapter(viewModel.workoutSessions.value ?: listOf()) { clicked: WorkoutSession -> onWorkoutSessionClicked(clicked) }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = workoutAdapter
        }

        viewModel.workoutSessions.observe(viewLifecycleOwner, Observer {
            workoutAdapter.setData(it ?: listOf())
        })

        // TODO: Show a dialog fragment with editor where we can CRUD new session
        addSessionButton.setOnClickListener {
            this.findNavController().navigate(R.id.action_destination_workout_session_screen_to_addWorkout)
//            viewModel.addWorkoutSession()
        }

        enableSwipeToDelete()
    }

    // TODO: Show a dialog fragment with editor where we can CRUD this session
    private fun onWorkoutSessionClicked(workoutSession: WorkoutSession) {
        Toast.makeText(requireContext(), "Clicked: ${workoutSession.date}", Toast.LENGTH_LONG).show()
    }

    private fun enableSwipeToDelete() {
        val simpleItemTouchCallback = object : SwipeToDeleteCallback(context = requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                val deletedWorkoutSession = viewModel.removeWorkoutSession(position)
                Snackbar.make(viewHolder.itemView, DELETED_WORKOUT_MSSG, Snackbar.LENGTH_LONG).apply {
                    setAction(UNDO_WORKOUT_MSSG) {
                        viewModel.restoreWorkoutSession(position, deletedWorkoutSession)
                    }
                    setActionTextColor(Color.YELLOW)
                    show()
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}
