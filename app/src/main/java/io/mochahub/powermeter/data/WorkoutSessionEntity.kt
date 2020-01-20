package io.mochahub.powermeter.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

@Entity(tableName = "workout_sessions")
data class WorkoutSessionEntity(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var date: Long,
    val createdAt: Long = Instant.now().epochSecond
)