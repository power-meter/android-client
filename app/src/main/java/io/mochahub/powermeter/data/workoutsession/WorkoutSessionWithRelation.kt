package io.mochahub.powermeter.data.workoutsession

import androidx.room.Embedded
import androidx.room.Relation
import io.mochahub.powermeter.data.workout.WorkoutEntity
import io.mochahub.powermeter.data.workout.WorkoutWithRelation

data class WorkoutSessionWithRelation(
    @Embedded
    val workoutSession: WorkoutSessionEntity,
    @Relation(parentColumn = "id", entityColumn = "workoutSessionUUID", entity = WorkoutEntity::class)
    val workouts: List<WorkoutWithRelation>
)