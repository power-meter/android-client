package io.mochahub.powermeter.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises ORDER BY createdAt ASC")
    fun getAll(): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises ORDER BY createdAt ASC")
    fun getAllAsList(): List<ExerciseEntity>

    @Query("SELECT * FROM exercises where name=:exerciseName")
    fun findByName(exerciseName: String): ExerciseEntity

    @Query("SELECT * FROM exercises where id=:exerciseID")
    fun findByID(exerciseID: String): ExerciseEntity

    @Insert
    suspend fun insertAll(vararg exercise: ExerciseEntity)

    @Delete
    suspend fun delete(exercise: ExerciseEntity)

    @Update
    suspend fun updateExercise(vararg exercise: ExerciseEntity)
}