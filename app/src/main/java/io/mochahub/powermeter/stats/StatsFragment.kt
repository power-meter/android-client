package io.mochahub.powermeter.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import io.mochahub.powermeter.R
import io.mochahub.powermeter.graph.GraphSharedViewModel
import kotlinx.android.synthetic.main.fragment_stats.statsRecyclerView

class StatsFragment : Fragment() {

    private val statsController by lazy {
        StatsController {
            sharedViewModel.select(it.exercise)
            navController.navigate(R.id.action_destination_stats_screen_to_graphFragment)
        }
    }

    private val navController by lazy { this.findNavController() }

    private val viewModel: StatsViewModel by viewModels()
    private val sharedViewModel by lazy {
        requireActivity().run {
            ViewModelProviders.of(this)[GraphSharedViewModel::class.java]
        }
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

        statsRecyclerView.setController(statsController)

        viewModel.stats.observe(viewLifecycleOwner, Observer {
            statsController.setData(it ?: emptyList())
        })
    }
}
