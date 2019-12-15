package io.mochahub.powermeter.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String,
    var muscleGroup: String,
    var personalRecord: Double,
    var createdAt: OffsetDateTime = OffsetDateTime.now()
)