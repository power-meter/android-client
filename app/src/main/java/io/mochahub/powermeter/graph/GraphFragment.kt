package io.mochahub.powermeter.graph

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import io.mochahub.powermeter.R
import io.mochahub.powermeter.shared.viewmodels.GraphSharedViewModel
import kotlinx.android.synthetic.main.graph_fragment.*

// TODO (ZAHIN): Should this be graph? Maybe we should find a way to the copy
//  paste nature of setting the title per fragment.
private const val TITLE = "Statistics"
private const val DATA_SET_LABEL = "Power Score"

class GraphFragment : Fragment() {

    private val viewModel: GraphViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.graph_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = TITLE

        val sharedViewModel = requireActivity().run {
            ViewModelProviders.of(this)[GraphSharedViewModel::class.java] }

        initGraph()
        ContextCompat.getColor(requireContext(), R.color.GraphText)

        viewModel.data.observe(viewLifecycleOwner, Observer {
            val lineDataSet = LineDataSet(viewModel.data.value, DATA_SET_LABEL)
                .apply {
                mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                lineWidth = 3.0f
                setDrawCircles(false)
                valueTextSize = 0.0f
            }
            val lineData = LineData(lineDataSet)
            graph.data = lineData
            graph.invalidate() // refresh
        })

        sharedViewModel.selectedExercise.observe(viewLifecycleOwner, Observer {
            graph_title.text = it.name
            graph_pr.text = it.personalRecord.toString()
        })
    }

    private fun initGraph() {
        graph.apply {
            setDrawBorders(false)
            setDrawGridBackground(false)
            setDrawMarkers(false)
            description.text = "" // TODO: Fill this in and move somewhere on graph

            xAxis.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)
            xAxis.isEnabled = false

            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawAxisLine(false)
            axisLeft.textSize = 15.0f
            axisLeft.textColor = ContextCompat.getColor(requireContext(), R.color.GraphText)

            axisRight.setDrawGridLines(false)
            axisRight.setDrawAxisLine(false)
            axisRight.isEnabled = false

            legend.isEnabled = false
            isDoubleTapToZoomEnabled = false
        }
    }
}
