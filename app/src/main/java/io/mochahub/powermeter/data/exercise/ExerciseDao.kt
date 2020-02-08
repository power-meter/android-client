package io.mochahub.powermeter.data.exercise

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises ORDER BY createdAt ASC")
    fun getAll(): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises where name=:exerciseName")
    suspend fun findByName(exerciseName: String): ExerciseEntity?

    @Query("SELECT * FROM exercises where id=:exerciseID")
    suspend fun findByID(exerciseID: String): ExerciseEntity

    @Insert
    suspend fun insertAll(vararg exercise: ExerciseEntity)

    @Delete
    suspend fun delete(exercise: ExerciseEntity)

    @Update
    suspend fun updateExercise(vararg exercise: ExerciseEntity)

    @Transaction
    fun upsert(exercise: ExerciseEntity) {
        val result = insert(exercise)
        if (result.toInt() < 0) {
            update(exercise)
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: ExerciseEntity): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(exercise: ExerciseEntity)
}