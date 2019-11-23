package io.mochahub.powermeter.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.mochahub.powermeter.R
import io.mochahub.powermeter.models.Card

class StatsFragmentViewModel : ViewModel() {
    var toggle: Boolean = false


    fun  getData() : List<Card> {
        val data = ArrayList<Card>()
        data.add(
            Card(
                "Hello World",
                "My Name is Zahin",
                R.drawable.balloon
            )
        )
        data.add(
            Card(
                "Hello World",
                "My Name is Zahin",
                R.drawable.balloon
            )
        )
        data.add(
            Card(
                "Hello World",
                "My Name is Zahin",
                R.drawable.balloon
            )
        )
        data.add(
            Card(
                "Hello World",
                "My Name is Zahin",
                R.drawable.balloon
            )
        )
        data.add(
            Card(
                "Hello World",
                "My Name is Zahin",
                R.drawable.balloon
            )
        )
        data.add(
            Card(
                "Hello World",
                "My Name is Zahin",
                R.drawable.common_google_signin_btn_icon_dark_focused
            )
        )
        data.add(
            Card(
                "Hello World",
                "My Name is Zahin",
                R.drawable.common_google_signin_btn_icon_light_focused
            )
        )
        data.add(
            Card(
                "Hello World",
                "My Name is Zahin",
                R.drawable.ic_launcher_background
            )
        )
        return data
    }

    fun setToggle(){
        toggle = !toggle
    }
}
