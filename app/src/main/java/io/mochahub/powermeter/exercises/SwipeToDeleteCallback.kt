package io.mochahub.powermeter.exercises

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class SwipeToDeleteCallback : ItemTouchHelper.SimpleCallback(0,
    ItemTouchHelper.LEFT
) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        target: ViewHolder
    ): Boolean {
        return false
    }
}