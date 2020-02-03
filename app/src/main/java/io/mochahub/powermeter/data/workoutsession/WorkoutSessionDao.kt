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
    @Transaction
    @Query("SELECT * FROM workout_sessions ORDER BY date DESC, createdAt DESC")
    fun getAllWithRelations(): Flow<List<WorkoutSessionWithRelation>>

    @Query("SELECT * FROM workout_sessions WHERE id = :workoutSessionID")
    suspend fun find(workoutSessionID: String): WorkoutSessionEntity

    @Insert
    suspend fun insertAll(vararg workoutSession: WorkoutSessionEntity)

    @Delete
    suspend fun delete(workoutSession: WorkoutSessionEntity)

    @Query("DELETE FROM workout_sessions WHERE id = :workoutSessionID")
    suspend fun delete(workoutSessionID: String)

    @Update
    suspend fun update(vararg workoutSession: WorkoutSessionEntity)
}
