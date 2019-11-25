package io.mochahub.powermeter.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String,
    var muscleGroup: String,
    var personalRecord: Double
)