package io.mochahub.powermeter.data.WorkoutSession

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutSessionDao {
    @Query("SELECT * FROM workout_sessions ORDER BY date DESC, createdAt DESC")
    fun getAll(): Flow<List<WorkoutSessionEntity>>

    @Query("DELETE FROM workout_sessions WHERE id = :workoutSessionID")
    suspend fun deleteByID(workoutSessionID: String)

    @Insert
    suspend fun insertAll(vararg workoutSession: WorkoutSessionEntity)

    @Delete
    suspend fun delete(workoutSession: WorkoutSessionEntity)

    @Update
    suspend fun update(vararg workoutSession: WorkoutSessionEntity)
}
