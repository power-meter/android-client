package io.mochahub.powermeter.workoutsession

import com.airbnb.epoxy.TypedEpoxyController
import io.mochahub.powermeter.data.workoutsession.WorkoutSessionEntity

class WorkoutSessionController(
    private val clickListener: (WorkoutSessionEntity) -> Unit
) : TypedEpoxyController<List<WorkoutSessionEntity>>() {

    override fun buildModels(workoutSessions: List<WorkoutSessionEntity>?) {
        workoutSessions?.forEach {
            workoutSessionRow(it, clickListener) {
                id(it.id)
            }
        }
    }
}
