package io.mochahub.powermeter.data.workoutsession

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

fun WorkoutSessionEntity.setCreatedAt(newCreatedAt: Long): WorkoutSessionEntity {
    return this.copy(createdAt = newCreatedAt)
}

fun WorkoutSessionEntity.setDate(newDate: Instant): WorkoutSessionEntity {
    return this.copy(date = newDate.epochSecond)
}