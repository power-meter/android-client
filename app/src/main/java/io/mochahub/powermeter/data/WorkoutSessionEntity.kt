package io.mochahub.powermeter.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "workout_sessions")
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String,
    var date: Long,
    var createdAt: Long = Instant.now().epochSecond
)