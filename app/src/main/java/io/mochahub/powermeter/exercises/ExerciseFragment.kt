package io.mochahub.powermeter.exercises

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.mochahub.powermeter.R
import io.mochahub.powermeter.SwipeToDeleteCallback
import io.mochahub.powermeter.models.Exercise
import kotlinx.android.synthetic.main.fragment_exercise.*

class ExerciseFragment : Fragment() {

    private val viewModel: ExerciseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exercise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exerciseAdapter = ExerciseAdapter(viewModel.exercises.value ?: listOf()) { clicked: Exercise -> onExerciseClick(clicked) }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = exerciseAdapter
        }

        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                val deletedExercise: Exercise = viewModel.removeExerciseAtPosition(position)
                Snackbar.make(viewHolder.itemView, "Exercise deleted!", Snackbar.LENGTH_LONG).apply {
                    setAction("UNDO") {
                        viewModel.restoreExerciseAtPosition(position, deletedExercise)
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
            EditExerciseFragment().show(fragmentManager!!,"TAG")
//            viewModel.addExercise(Exercise(name = "New Exercise", personalRecord = 88f, muscleGroup = "New Group"))
        }
    }

    private fun onExerciseClick(exercise: Exercise) {
        Toast.makeText(requireContext(), "Clicked: ${exercise.name}", Toast.LENGTH_SHORT).show()
    }
}
