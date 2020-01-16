package io.mochahub.powermeter.workoutsession.workoutsessiondialog

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import com.airbnb.epoxy.EpoxyTouchHelper
import io.mochahub.powermeter.R
import io.mochahub.powermeter.models.WorkoutSet
import kotlin.math.absoluteValue

class WorkoutRowSetSwipeCallBack(
    private val resources: Resources,
    private val onLeftSwipe: (WorkoutSet) -> Unit
) : EpoxyTouchHelper.SwipeCallbacks<WorkoutRowSetModel>() {

    private val paint = Paint()

    init {
        paint.setARGB(255, 255, 0, 0)
    }

    private fun getDeleteIcon(): Bitmap {
        val vectorDrawable = (resources.getDrawable(R.drawable.ic_delete_white_24dp, null))
        val icon = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(icon)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return icon
    }

    override fun onSwipeCompleted(
        model: WorkoutRowSetModel?,
        itemView: View?,
        position: Int,
        direction: Int
    ) {
        if (direction == (ItemTouchHelper.LEFT) && model != null) {
            onLeftSwipe(model.workoutSet)
        }
    }

    override fun onSwipeProgressChanged(
        model: WorkoutRowSetModel?,
        itemView: View?,
        swipeProgress: Float,
        canvas: Canvas?
    ) {
        super.onSwipeProgressChanged(model, itemView, swipeProgress, canvas)
        if (itemView != null && canvas != null && model != null) {
            val icon = getDeleteIcon()

            val backgroundRect = RectF(
                (itemView.width.toFloat() - itemView.width.toFloat()*(swipeProgress.absoluteValue)),
                itemView.top.toFloat(),
                itemView.width.toFloat(), itemView.bottom.toFloat()
            )
            canvas.drawRect(backgroundRect, paint)

            val width = (itemView.bottom.toFloat() - itemView.top.toFloat()) / 3
            val iconDest = RectF(
                itemView.right.toFloat() - 2 * width,
                itemView.top.toFloat() + width,
                itemView.right.toFloat() - width,
                itemView.bottom.toFloat() - width
            )
            if (backgroundRect.left <= iconDest.left) {
                canvas.drawBitmap(icon, null, iconDest, paint)
            }
        }
    }
}