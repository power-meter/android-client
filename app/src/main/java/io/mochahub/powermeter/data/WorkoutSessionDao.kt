package io.mochahub.powermeter.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WorkoutSessionDao {
    @Query("SELECT * FROM workout_sessions ORDER BY createdAt ASC")
    fun getAll(): LiveData<List<WorkoutSessionEntity>>

    @Insert
    suspend fun insertAll(vararg workoutSession: WorkoutSessionEntity)

    @Delete
    suspend fun delete(workoutSession: WorkoutSessionEntity)

    @Update
    suspend fun update(vararg workoutSession: WorkoutSessionEntity)
}