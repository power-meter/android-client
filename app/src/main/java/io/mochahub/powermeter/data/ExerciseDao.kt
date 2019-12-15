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

    @Query("SELECT * FROM exercises WHERE id = :id")
    fun getById(id: Int): LiveData<ExerciseEntity>

    @Insert
    fun insertAll(vararg exercise: ExerciseEntity)

    @Delete
    fun delete(exercise: ExerciseEntity)

    @Update
    fun updateExercise(vararg exercise: ExerciseEntity)
}