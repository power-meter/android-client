package io.mochahub.powermeter.data.workout

import androidx.room.Embedded
import androidx.room.Relation
import io.mochahub.powermeter.data.exercise.ExerciseEntity
import io.mochahub.powermeter.data.exercise.toModel
import io.mochahub.powermeter.data.workoutset.WorkoutSetEntity
import io.mochahub.powermeter.models.Workout
import io.mochahub.powermeter.models.WorkoutSet

data class WorkoutWithRelation(
    @Embedded
    val workout: WorkoutEntity,
    @Relation(parentColumn = "exerciseUUID", entityColumn = "id", entity = ExerciseEntity::class)
    val exercise: ExerciseEntity,
    @Relation(parentColumn = "id", entityColumn = "workoutUUID", entity = WorkoutSetEntity::class)
    val workoutSets: List<WorkoutSetEntity>
)

fun WorkoutWithRelation.toModel(): Workout {
    return Workout(id = this.workout.id,
        exercise = this.exercise.toModel(),
        sets = this.workoutSets.map { WorkoutSet(id = it.id, weight = it.weight, reps = it.reps) }
                as MutableList<WorkoutSet>)
}