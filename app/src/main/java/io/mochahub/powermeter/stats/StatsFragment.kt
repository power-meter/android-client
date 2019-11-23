package io.mochahub.powermeter.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.mochahub.powermeter.R
import io.mochahub.powermeter.common.ui.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_stats.*
import android.widget.CompoundButton





class StatsFragment : Fragment() {

    private lateinit var cardAdapter : StatsRecyclerAdapater
    private lateinit var viewModel: StatsFragmentViewModel

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
        viewModel = ViewModelProviders.of(this).get(StatsFragmentViewModel::class.java)

        initRecyclerView()
        // TODO: Use Livedata and diffutil
        addDataSet()

        initToggle()

    }

    private fun initToggle() {

    }

    private fun addDataSet(){
        cardAdapter.setList(viewModel.getData())
    }

    private fun initRecyclerView(){
        card_list.apply{
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(TopSpacingItemDecoration(30))
            cardAdapter = StatsRecyclerAdapater()
            adapter = cardAdapter
        }
    }
}
