package io.mochahub.powermeter.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.mochahub.powermeter.R
import kotlinx.android.synthetic.main.fragment_stats.*

class StatsFragment : Fragment() {

    private lateinit var statsAdapter : StatsAdapter
    private val viewModel: StatsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        stats_list.apply{
            layoutManager = LinearLayoutManager(context)
            statsAdapter = StatsAdapter(viewModel.stats.value ?: listOf()){
                Toast.makeText(requireContext(), "Clicked: ${it.exercise.name}", Toast.LENGTH_SHORT).show()
            }
            adapter = statsAdapter
        }

        viewModel.stats.observe(viewLifecycleOwner, Observer {
            statsAdapter.setData(it ?: listOf())
        })
    }

}
