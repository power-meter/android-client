package io.mochahub.powermeter.data.Exercise

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime
import java.util.UUID

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var name: String,
    var muscleGroup: String,
    var personalRecord: Double,
    val createdAt: OffsetDateTime = OffsetDateTime.now()
)