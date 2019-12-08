package io.mochahub.powermeter.exercises

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.mochahub.powermeter.R
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.data.Exercise
import io.mochahub.powermeter.shared.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.fragment_exercise.*

private const val TITLE = "Exercises"

class ExerciseFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exercise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = TITLE

        val db = AppDatabase(requireContext())
        val viewModel = ExerciseViewModel(db = db)

        val exerciseAdapter = ExerciseAdapter(viewModel.exercises.value ?: listOf()) { clicked: Exercise -> onExerciseClick(clicked) }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = exerciseAdapter
        }

        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                val deletedExercise: Exercise = viewModel.removeExercise(position)
                Snackbar.make(viewHolder.itemView, "Exercise deleted!", Snackbar.LENGTH_LONG).apply {
                    setAction("UNDO") {
                        viewModel.addExercise(deletedExercise)
                    }
                    setActionTextColor(Color.YELLOW)
                    show()
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        viewModel.exercises.observe(viewLifecycleOwner, Observer {
            exerciseAdapter.setData(it ?: listOf())
        })

        // TODO: Show a dialog fragment with editor where we can write name of new exercise
        addExerciseBtn.setOnClickListener {
            viewModel.addExercise(Exercise(name = "New Exercise", personalRecord = 88.0, muscleGroup = "New Group"))
        }
    }

    private fun onExerciseClick(exercise: Exercise) {
        Toast.makeText(requireContext(), "Clicked: ${exercise.name}", Toast.LENGTH_SHORT).show()
    }
}
