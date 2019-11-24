package io.mochahub.powermeter.graph

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import io.mochahub.powermeter.R
import android.R.attr.entries
import android.graphics.Color
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.graph_fragment.*
import com.github.mikephil.charting.data.LineData




class GraphFragment : Fragment() {

    companion object {
        fun newInstance() = GraphFragment()
    }

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
        viewModel.data.observe(viewLifecycleOwner, Observer {
            val lineDataSet = LineDataSet(viewModel.data.value,"Power Score")
//            lineDataSet.color= (R.color.colorAccent)
//            lineDataSet.setCircleColor(R.color.colorAccent)

            lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER)
            lineDataSet.lineWidth = 3.0f
            lineDataSet.setDrawCircles(false)
//            lineDataSet.circleRadius=6.0f
//            lineDataSet.circleHoleRadius = 3.0f
//            lineDataSet.setColor(R.color.colorPrimary);
//            lineDataSet.setValueTextColor(Color.parseColor("#FF775F"));
            val lineData = LineData(lineDataSet)
            graph.setData(lineData);
            graph.invalidate(); // refresh
        })



    }

    private fun initGraph() {
        graph.setDrawBorders(false)
        graph.setDrawGridBackground(false)
        graph.setDrawMarkers(false)
        graph.description.text=""

        graph.xAxis.setDrawAxisLine(false)
        graph.xAxis.setDrawGridLines(false)
        graph.xAxis.isEnabled = false

        graph.axisLeft.setDrawGridLines(false)
        graph.axisLeft.setDrawAxisLine(false)
        graph.axisLeft.textSize = 15.0f
//        graph.axisLeft.isEnabled = false

        graph.axisRight.setDrawGridLines(false)
        graph.axisRight.setDrawAxisLine(false)
        graph.axisRight.isEnabled = false

        graph.legend.isEnabled = false

    }

}
