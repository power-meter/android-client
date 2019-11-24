package io.mochahub.powermeter.graph

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.mochahub.powermeter.R
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.graph_fragment.*
import com.github.mikephil.charting.data.LineData

class GraphFragment : Fragment() {
    
    private val viewModel: GraphViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.graph_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGraph()
        ContextCompat.getColor(requireContext(), R.color.GraphText)

        viewModel.data.observe(viewLifecycleOwner, Observer {
            val lineDataSet = LineDataSet(viewModel.data.value,"Power Score").
                apply {
                mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                lineWidth = 3.0f
                setDrawCircles(false)
                valueTextSize = 0.0f
            }
            val lineData = LineData(lineDataSet)
            graph.data = lineData
            graph.invalidate() // refresh
        })
    }


    private fun initGraph() {
        graph.apply {
            setDrawBorders(false)
            setDrawGridBackground(false)
            setDrawMarkers(false)
            description.text ="" // TODO: Fill this in and move somehwere on graph

            xAxis.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)
            xAxis.isEnabled = false

            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawAxisLine(false)
            axisLeft.textSize = 15.0f
            axisLeft.textColor = ContextCompat.getColor(this.context!!, R.color.GraphText)

            axisRight.setDrawGridLines(false)
            axisRight.setDrawAxisLine(false)
            axisRight.isEnabled = false

            legend.isEnabled = false
        }
    }
}
