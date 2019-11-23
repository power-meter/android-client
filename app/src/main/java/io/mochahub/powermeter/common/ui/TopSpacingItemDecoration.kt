package io.mochahub.powermeter.common.ui

import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class TopSpacingItemDecoration (private val padding:Int): RecyclerView.ItemDecoration(){
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = padding
    }

}
