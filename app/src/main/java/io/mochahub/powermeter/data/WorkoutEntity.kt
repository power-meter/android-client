package io.mochahub.powermeter.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.OffsetDateTime
import java.util.UUID

// TODO: Add indices
@Entity(tableName = "workouts", foreignKeys = arrayOf(
    ForeignKey(
        entity = WorkoutEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("workoutSessionID"),
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = ExerciseEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("exerciseID"),
        onDelete = ForeignKey.CASCADE
    )
))
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var workoutSessionID: UUID,
    var exerciseID: UUID,
    var createdAt: OffsetDateTime = OffsetDateTime.now()
)