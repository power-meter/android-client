package io.mochahub.powermeter.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

// TODO: Add indices
@Entity(tableName = "workouts", foreignKeys = arrayOf(
    ForeignKey(
        entity = WorkoutSessionEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("workoutSessionUUID"),
        onDelete = ForeignKey.CASCADE,
        deferred = true
    ),
    ForeignKey(
        entity = ExerciseEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("exerciseUUID"),
        onDelete = ForeignKey.CASCADE,
        deferred = true
    )
))
data class WorkoutEntity(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var workoutSessionUUID: String,
    var exerciseUUID: String,
    val createdAt: Long = Instant.now().epochSecond
)