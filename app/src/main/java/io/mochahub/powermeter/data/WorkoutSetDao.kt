package io.mochahub.powermeter.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.mochahub.powermeter.models.WorkoutSet
import java.util.UUID

@Dao
interface WorkoutSetDao {
    @Query("SELECT * FROM workout_sets ORDER BY createdAt ASC")
    fun getAll(): LiveData<List<WorkoutSet>>

    @Query("SELECT * FROM workout_sets where workoutID= :workoutID")
    fun getWorkoutSetsByWorkout(workoutID: UUID): LiveData<List<WorkoutSetEntity>>

    @Query("SELECT * FROM workout_sets where workoutSessionID= :workoutSessionID")
    fun getWorkoutSetsByWorkoutSession(workoutSessionID: UUID): LiveData<List<WorkoutSetEntity>>

    @Insert
    suspend fun insertAll(vararg workoutSet: WorkoutSetEntity)

    @Delete
    suspend fun delete(workoutSet: WorkoutSetEntity)

    @Update
    suspend fun update(vararg workoutSet: WorkoutSetEntity)
}