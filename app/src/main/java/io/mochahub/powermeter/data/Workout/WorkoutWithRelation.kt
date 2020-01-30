package io.mochahub.powermeter.data.Workout

import androidx.room.Embedded
import androidx.room.Relation
import io.mochahub.powermeter.data.Exercise.ExerciseEntity
import io.mochahub.powermeter.data.WorkoutSet.WorkoutSetEntity

data class WorkoutWithRelation(
    @Embedded
    val workout: WorkoutEntity,
    @Relation(parentColumn = "exerciseUUID", entityColumn = "id", entity = ExerciseEntity::class)
    val exercise: ExerciseEntity,
    @Relation(parentColumn = "id", entityColumn = "workoutUUID", entity = WorkoutSetEntity::class)
    val workoutSets: List<WorkoutSetEntity>
)