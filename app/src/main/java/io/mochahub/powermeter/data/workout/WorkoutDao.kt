package io.mochahub.powermeter.data.workout

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Transaction
    @Query("SELECT * FROM workouts where workoutSessionUUID= :workoutSessionUUID")
    fun getWorkoutsByWorkoutSession(workoutSessionUUID: String): Flow<List<WorkoutWithRelation>>

    @Insert
    suspend fun insertAll(vararg workout: WorkoutEntity)

    @Delete
    suspend fun delete(workout: WorkoutEntity)

    @Update
    suspend fun update(vararg workout: WorkoutEntity)
}