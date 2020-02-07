package io.mochahub.powermeter.data.exercise

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.mochahub.powermeter.models.Exercise
import java.time.OffsetDateTime
import java.util.UUID

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var name: String,
    var muscleGroup: String,
    var personalRecord: Double,
    val createdAt: OffsetDateTime = OffsetDateTime.now()
)

fun ExerciseEntity.toModel(): Exercise {
    return Exercise(
        name = this.name, personalRecord = this.personalRecord, muscleGroup = this.muscleGroup)
}

fun ExerciseEntity.update(
    newName: String,
    newMuscleGroup: String,
    newPersonalRecord: Double
): ExerciseEntity {
    return this.copy(
        name = newName,
        muscleGroup = newMuscleGroup,
        personalRecord = newPersonalRecord)
}

fun ExerciseEntity.update(
    id: String,
    newName: String,
    newMuscleGroup: String,
    newPersonalRecord: Double
): ExerciseEntity {
    return this.copy(
        id = id,
        name = newName,
        muscleGroup = newMuscleGroup,
        personalRecord = newPersonalRecord)
}

fun ExerciseEntity.update(newPersonalRecord: Double): ExerciseEntity {
    return this.copy(personalRecord = newPersonalRecord)
}