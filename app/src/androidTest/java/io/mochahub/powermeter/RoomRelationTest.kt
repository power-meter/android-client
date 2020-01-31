package io.mochahub.powermeter

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.data.exercise.ExerciseDao
import io.mochahub.powermeter.data.exercise.ExerciseEntity
import io.mochahub.powermeter.data.workout.WorkoutDao
import io.mochahub.powermeter.data.workout.WorkoutEntity
import io.mochahub.powermeter.data.workoutsession.WorkoutSessionDao
import io.mochahub.powermeter.data.workoutsession.WorkoutSessionEntity
import io.mochahub.powermeter.data.workoutset.WorkoutSetDao
import io.mochahub.powermeter.data.workoutset.WorkoutSetEntity
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.time.Instant

class RoomRelationTest {
    private lateinit var exerciseDao: ExerciseDao
    private lateinit var workoutSetDao: WorkoutSetDao
    private lateinit var workoutDao: WorkoutDao
    private lateinit var workoutSessionDao: WorkoutSessionDao
    private lateinit var workoutSessionEntity: WorkoutSessionEntity
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        exerciseDao = db.exerciseDao()
        workoutSetDao = db.workoutSetDao()
        workoutDao = db.workoutDao()
        workoutSessionDao = db.workoutSessionDao()

        runBlocking {
            val exerciseEntity = ExerciseEntity(name = "Bench Press", muscleGroup = "Chest", personalRecord = 135.0)
            exerciseDao.insertAll(exerciseEntity)

            workoutSessionEntity = WorkoutSessionEntity(date = Instant.now().epochSecond)
            workoutSessionDao.insertAll(workoutSessionEntity)

            val workoutEntity =
                WorkoutEntity(workoutSessionUUID = workoutSessionEntity.id, exerciseUUID = exerciseEntity.id)
            workoutDao.insertAll(workoutEntity)

            val workoutSetsEntity = listOf(
                WorkoutSetEntity(workoutSessionUUID = workoutSessionEntity.id, workoutUUID = workoutEntity.id, reps = 10, weight = 100.0),
                WorkoutSetEntity(workoutSessionUUID = workoutSessionEntity.id, workoutUUID = workoutEntity.id, reps = 10, weight = 115.0),
                WorkoutSetEntity(workoutSessionUUID = workoutSessionEntity.id, workoutUUID = workoutEntity.id, reps = 10, weight = 125.0)
            )
            workoutSetDao.insertAll(*workoutSetsEntity.toTypedArray())
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun getWorkoutSessionWithRelations() {
        var workoutSessions = runBlocking { workoutSessionDao.getAllWithRelations().take(1).toList() }

        assertTrue(workoutSessions.isNotEmpty())
        assertTrue(workoutSessions.size == 1)
        assertTrue(workoutSessions[0].workouts.size == 1)
        assertTrue(workoutSessions[0].workouts[0].workoutSets.size == 3)
        println(workoutSessions)
    }
}
