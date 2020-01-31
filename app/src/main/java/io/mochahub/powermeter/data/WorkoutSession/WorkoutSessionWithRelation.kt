package io.mochahub.powermeter.data.WorkoutSession

import androidx.room.Embedded
import androidx.room.Relation
import io.mochahub.powermeter.data.Workout.WorkoutEntity
import io.mochahub.powermeter.data.Workout.WorkoutWithRelation

data class WorkoutSessionWithRelation(
    @Embedded
    val workoutSession: WorkoutSessionEntity,
    @Relation(parentColumn = "id", entityColumn = "workoutSessionUUID", entity = WorkoutEntity::class)
    val workouts: List<WorkoutWithRelation>
)