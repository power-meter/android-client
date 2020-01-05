package io.mochahub.powermeter.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.Instant

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
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var workoutSessionID: Int,
    var exerciseID: Int,
    var createdAt: Long = Instant.now().epochSecond
)