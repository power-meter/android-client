package io.mochahub.powermeter.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WorkoutSetDao {
    @Query("SELECT * FROM workout_sets where workoutUUID= :workoutID")
    fun getWorkoutSetsByWorkout(workoutID: String): List<WorkoutSetEntity>

    @Insert
    suspend fun insertAll(vararg workoutSet: WorkoutSetEntity)

    @Delete
    suspend fun delete(workoutSet: WorkoutSetEntity)

    @Update
    suspend fun update(vararg workoutSet: WorkoutSetEntity)
}