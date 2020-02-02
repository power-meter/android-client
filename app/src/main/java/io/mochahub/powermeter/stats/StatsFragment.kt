package io.mochahub.powermeter.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import io.mochahub.powermeter.R
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.exercises.ExerciseController
import kotlinx.android.synthetic.main.fragment_stats.statsRecyclerView

class StatsFragment : Fragment() {

    private val exerciseController by lazy {
        ExerciseController {
            val action = StatsFragmentDirections.actionDestinationStatsScreenToGraphFragment(it.id, it.personalRecord.toString())
            navController.navigate(action)
        }
    }

    private val navController by lazy { this.findNavController() }

    private val viewModel by lazy {
        ViewModelProviders.of(
            this,
            StatsViewModel(AppDatabase(requireContext()).exerciseDao())
        )[StatsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = resources.getString(R.string.stats_screen_label)

        statsRecyclerView.setController(exerciseController)

        viewModel.exercises.observe(viewLifecycleOwner, Observer {
            exerciseController.setData(it ?: emptyList())
        })
    }
}
