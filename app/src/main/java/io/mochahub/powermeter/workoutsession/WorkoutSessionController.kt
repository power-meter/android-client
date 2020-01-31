package io.mochahub.powermeter.workoutsession

import com.airbnb.epoxy.TypedEpoxyController
import io.mochahub.powermeter.data.workoutsession.WorkoutSessionWithRelation

class WorkoutSessionController(
    private val clickListener: (WorkoutSessionWithRelation) -> Unit
) : TypedEpoxyController<List<WorkoutSessionWithRelation>>() {

    override fun buildModels(workoutSessions: List<WorkoutSessionWithRelation>?) {
        workoutSessions?.forEach {
            workoutSessionRow(it, clickListener) {
                id(it.workoutSession.id)
            }
        }
    }
}
