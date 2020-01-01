package io.mochahub.powermeter.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises ORDER BY createdAt ASC")
    fun getAll(): LiveData<List<ExerciseEntity>>

    @Insert
    suspend fun insertAll(vararg exercise: ExerciseEntity)

    @Delete
    suspend fun delete(exercise: ExerciseEntity)

    @Update
    suspend fun updateExercise(vararg exercise: ExerciseEntity)
}