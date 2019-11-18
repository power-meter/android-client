package io.mochahub.powermeter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() : MainFragment {
            return MainFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        exerciseButton.setOnClickListener {
            fragmentManager
                ?.beginTransaction()
                ?.replace(R.id.fragmentLayout, ExerciseFragment.newInstance(), "exerciseFragment")
                ?.addToBackStack("mainFragment")
                ?.commit()
        }

        workoutButton.setOnClickListener {
            fragmentManager
                ?.beginTransaction()
                ?.replace(R.id.fragmentLayout, WorkoutFragment.newInstance(), "workoutFragment")
                ?.addToBackStack("mainFragment")
                ?.commit()
        }

        statsButton.setOnClickListener {
            fragmentManager
                ?.beginTransaction()
                ?.replace(R.id.fragmentLayout, StatsFragment.newInstance(), "statsFragment")
                ?.addToBackStack("mainFragment")
                ?.commit()
        }
    }

}
