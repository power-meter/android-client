package io.mochahub.powermeter.graph

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.mochahub.powermeter.R
import kotlinx.android.synthetic.main.graph_fragment.graph
import kotlinx.android.synthetic.main.graph_fragment.graph_pr
import kotlinx.android.synthetic.main.graph_fragment.graph_title
import kotlinx.android.synthetic.main.graph_fragment.scrub_value

// TODO (ZAHIN): Should this be graph? Maybe we should find a way to the copy
//  paste nature of setting the title per fragment.
private const val DATA_SET_LABEL = "Power Score"

class GraphFragment : Fragment() {

    private val viewModel: GraphViewModel by viewModels()
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

        val sharedViewModel = requireActivity().run {
            ViewModelProviders.of(this)[GraphSharedViewModel::class.java]
        }

        graph.adapter = graphAdapter

        graph.setScrubListener {
            if (it != null) {
                val value: Float = it as Float
                scrub_value.text = value.toString()
            } else {
                scrub_value.text = getString(R.string.scrub_empty)
            }
        }

        viewModel.data.observe(viewLifecycleOwner, Observer {
            graphAdapter.setData(it ?: emptyList())
        })

        sharedViewModel.selectedExercise.observe(viewLifecycleOwner, Observer {
            graph_title.text = it.name
            graph_pr.text = it.personalRecord.toString()
        })
    }
}
