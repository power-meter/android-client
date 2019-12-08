package io.mochahub.powermeter.workouts.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.mochahub.powermeter.R

class AddWorkout : Fragment() {

    companion object {
        fun newInstance() = AddWorkout()
    }

    private lateinit var viewModel: AddWorkoutViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_workout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddWorkoutViewModel::class.java)
        // TODO: Use the ViewModel
    }
}