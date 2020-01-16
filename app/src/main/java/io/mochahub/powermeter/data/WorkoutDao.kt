package io.mochahub.powermeter.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workouts where workoutSessionUUID= :workoutSessionUUID")
    fun getWorkoutsByWorkoutSession(workoutSessionUUID: String): List<WorkoutEntity>

    @Insert
    suspend fun insertAll(vararg workout: WorkoutEntity)

    @Delete
    suspend fun delete(workout: WorkoutEntity)

    @Update
    suspend fun update(vararg workout: WorkoutEntity)
}