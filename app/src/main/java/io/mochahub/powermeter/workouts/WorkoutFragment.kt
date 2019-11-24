package io.mochahub.powermeter.workouts

import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.mochahub.powermeter.R
import io.mochahub.powermeter.models.WorkoutSession
import kotlinx.android.synthetic.main.fragment_workout.*

class WorkoutFragment : Fragment() {

    private val viewModel: WorkoutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val workoutAdapter = WorkoutAdapter(viewModel.workoutSessions.value ?: listOf()) { clicked : WorkoutSession -> onWorkoutSessionClicked(clicked) }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = workoutAdapter
        }

        viewModel.workoutSessions.observe(viewLifecycleOwner, Observer {
            workoutAdapter.setData(it ?: listOf())
        })

        // TODO: Show a dialog fragment with editor where we can CRUD new session
        addSessionButton.setOnClickListener {
            viewModel.addWorkoutSession()
        }

        enableSwipeToDelete()
    }

    // TODO: Show a dialog fragment with editor where we can CRUD this session
    private fun onWorkoutSessionClicked(workoutSession: WorkoutSession) {
        Toast.makeText(requireContext(), "Clicked: ${workoutSession.date}", Toast.LENGTH_LONG).show()
    }

    private fun enableSwipeToDelete() {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                val deletedWorkoutSession = viewModel.removeWorkoutSession(position)
                Snackbar.make(viewHolder.itemView, "Workout session deleted!", Snackbar.LENGTH_LONG).apply {
                    setAction("UNDO") {
                        viewModel.restoreWorkoutSession(position, deletedWorkoutSession)
                    }
                    setActionTextColor(Color.YELLOW)
                    show()
                }
            }

            private fun getDeleteIcon(): Bitmap {
                val vectorDrawable = (context!!.getDrawable(R.drawable.ic_delete_white_24dp))!!
                val icon = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(icon)
                vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
                vectorDrawable.draw(canvas)

                return icon
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val icon = getDeleteIcon()
                    val itemView = viewHolder.itemView
                    val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                    val width = height / 3
                    val p = Paint()

                    p.color = Color.parseColor("#D32F2F")

                    val background: RectF
                    val iconDest: RectF

                    if (dX > 0) {
                        background = RectF(
                            itemView.left.toFloat(),
                            itemView.top.toFloat(),
                            dX,
                            itemView.bottom.toFloat()
                        )
                        iconDest = RectF(
                            itemView.left.toFloat() + width,
                            itemView.top.toFloat() + width,
                            itemView.left.toFloat() + 2 * width,
                            itemView.bottom.toFloat() - width
                        )
                    } else {
                        background = RectF(
                            itemView.right.toFloat() + dX,
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat()
                        )
                        iconDest = RectF(
                            itemView.right.toFloat() - 2 * width,
                            itemView.top.toFloat() + width,
                            itemView.right.toFloat() - width,
                            itemView.bottom.toFloat() - width
                        )
                    }

                    c.drawRect(background, p)
                    c.drawBitmap(icon, null, iconDest, p)
                }

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}
