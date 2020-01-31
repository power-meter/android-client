package io.mochahub.powermeter.data.workoutset

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import io.mochahub.powermeter.data.workout.WorkoutEntity
import io.mochahub.powermeter.data.workoutsession.WorkoutSessionEntity
import java.time.Instant
import java.util.UUID

// TODO: Add indices
@Entity(
    tableName = "workout_sets",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutSessionEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("workoutSessionUUID"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            deferred = true
        ), ForeignKey(
            entity = WorkoutEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("workoutUUID"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            deferred = true
        )
    ]
)
data class WorkoutSetEntity(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    val workoutSessionUUID: String,
    val workoutUUID: String,
    var reps: Int,
    var weight: Double,
    val createdAt: Long = Instant.now().epochSecond
)