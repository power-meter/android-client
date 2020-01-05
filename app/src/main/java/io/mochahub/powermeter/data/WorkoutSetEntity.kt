package io.mochahub.powermeter.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.Instant

// TODO: Add indices
@Entity(tableName = "workout_sets", foreignKeys = arrayOf(
    ForeignKey(
        entity = WorkoutSessionEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("workoutSessionID"),
        onDelete = ForeignKey.NO_ACTION
    ),
    ForeignKey(
        entity = WorkoutEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("workoutID"),
        onDelete = ForeignKey.NO_ACTION
    )
))data class WorkoutSetEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var workoutSessionID: Int,
    var workoutID: Int,
    var reps: Int,
    var weight: Double,
    var createdAt: Long = Instant.now().epochSecond
)