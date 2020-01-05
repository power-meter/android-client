package io.mochahub.powermeter.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workouts ORDER BY createdAt ASC")
    fun getAll(): LiveData<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts where workoutSessionID= :workoutSessionID")
    fun getWorkoutsByWorkoutSession(workoutSessionID: Int): LiveData<List<WorkoutEntity>>

    @Insert
    suspend fun insertAll(vararg workout: WorkoutEntity)

    @Delete
    suspend fun delete(workout: WorkoutEntity)

    @Update
    suspend fun update(vararg workout: WorkoutEntity)
}