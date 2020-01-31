package io.mochahub.powermeter.data.workoutsession

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutSessionDao {
    @Query("SELECT * FROM workout_sessions ORDER BY date DESC, createdAt DESC")
    fun getAll(): Flow<List<WorkoutSessionEntity>>

    @Transaction
    @Query("SELECT * FROM workout_sessions")
    fun getAllWithRelations(): Flow<List<WorkoutSessionWithRelation>>

    @Query("DELETE FROM workout_sessions WHERE id = :workoutSessionID")
    suspend fun deleteByID(workoutSessionID: String)

    @Insert
    suspend fun insertAll(vararg workoutSession: WorkoutSessionEntity)

    @Delete
    suspend fun delete(workoutSession: WorkoutSessionEntity)

    @Update
    suspend fun update(vararg workoutSession: WorkoutSessionEntity)
}
