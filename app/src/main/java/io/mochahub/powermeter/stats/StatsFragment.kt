package io.mochahub.powermeter.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.mochahub.powermeter.R
import kotlinx.android.synthetic.main.fragment_stats.*





class StatsFragment : Fragment() {

    private lateinit var statsAdapter : StatsAdapter
    private lateinit var viewModel: StatsViewModel

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
        viewModel = ViewModelProviders.of(this).get(StatsViewModel::class.java)
        initRecyclerView()
        initData()
    }


    private fun initData(){
        viewModel.stats.observe(viewLifecycleOwner, Observer {
            statsAdapter.setList(it ?: listOf())
        })
    }

    private fun initRecyclerView(){
        card_list.apply{
            layoutManager = LinearLayoutManager(context)
            statsAdapter = StatsAdapter()
            adapter = statsAdapter
        }
    }
}
