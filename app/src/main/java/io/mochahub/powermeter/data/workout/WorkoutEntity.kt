package io.mochahub.powermeter.data.workout

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import io.mochahub.powermeter.data.exercise.ExerciseEntity
import io.mochahub.powermeter.data.workoutsession.WorkoutSessionEntity
import java.time.Instant
import java.util.UUID

// TODO: Add indices
@Entity(
    tableName = "workouts",
    foreignKeys = [
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
    ]
)
data class WorkoutEntity(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var workoutSessionUUID: String,
    var exerciseUUID: String,
    val createdAt: Long = Instant.now().epochSecond
)