package io.mochahub.powermeter.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity(tableName = "workout_sessions")
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String,
    var date: OffsetDateTime,
    var createdAt: OffsetDateTime = OffsetDateTime.now()
)