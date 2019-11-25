package io.mochahub.powermeter.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises")
    fun getAll(): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE id = :id")
    fun getById(id: Int): LiveData<Exercise>

    @Insert
    fun insertAll(vararg exercise: Exercise)

    @Delete
    fun delete(exercise: Exercise)

    @Update
    fun updateExercise(vararg exercise: Exercise)
}