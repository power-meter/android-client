package io.mochahub.powermeter.graph

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import io.mochahub.powermeter.R
import io.mochahub.powermeter.data.AppDatabase
import kotlinx.android.synthetic.main.graph_fragment.emptyStateText
import kotlinx.android.synthetic.main.graph_fragment.graph
import kotlinx.android.synthetic.main.graph_fragment.scrub_value

class GraphFragment : Fragment() {

    private val args: GraphFragmentArgs by navArgs()

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            GraphViewModel(
                args.exerciseID,
                args.personalRecord.toDouble(),
                AppDatabase(requireContext()).workoutDao()
            )
        ).get(GraphViewModel::class.java)
    }
    private val graphAdapter = GraphAdapter(emptyList())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.graph_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = resources.getString(R.string.stats_screen_label)

        showEmptyState()

        graph.adapter = graphAdapter
        // TODO: Create a model that has score and date so that we can show the date when scrubbing
        graph.setScrubListener {
            if (it != null) {
                val value: Float = it as Float
                scrub_value.text = value.toString()
            } else {
                scrub_value.text = getString(R.string.scrub_empty)
            }
        }

        viewModel.data.observe(viewLifecycleOwner, Observer {
            if (it.size < 5) {
                showEmptyState()
            } else {
                showGraph()
                graphAdapter.setData(it ?: emptyList())
            }
        })

        viewModel.workouts.observe(viewLifecycleOwner, Observer {
            viewModel.createPowerScore(it)
        })
    }

    private fun showEmptyState() {
        graph.visibility = View.INVISIBLE
        scrub_value.visibility = View.INVISIBLE
        emptyStateText.visibility = View.VISIBLE
    }

    private fun showGraph() {
        graph.visibility = View.VISIBLE
        scrub_value.visibility = View.VISIBLE
        emptyStateText.visibility = View.INVISIBLE
    }
}
