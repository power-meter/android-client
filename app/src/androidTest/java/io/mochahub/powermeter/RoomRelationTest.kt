package io.mochahub.powermeter

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.data.Exercise.ExerciseDao
import io.mochahub.powermeter.data.Exercise.ExerciseEntity
import io.mochahub.powermeter.data.Workout.WorkoutDao
import io.mochahub.powermeter.data.Workout.WorkoutEntity
import io.mochahub.powermeter.data.WorkoutSession.WorkoutSessionDao
import io.mochahub.powermeter.data.WorkoutSession.WorkoutSessionEntity
import io.mochahub.powermeter.data.WorkoutSet.WorkoutSetDao
import io.mochahub.powermeter.data.WorkoutSet.WorkoutSetEntity
import kotlinx.coroutines.flow.toList
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.Instant

class RoomRelationTest {
    @RunWith(AndroidJUnit4::class)
    class SimpleEntityReadWriteTest {
        private lateinit var exerciseDao: ExerciseDao
        private lateinit var workoutSetDao: WorkoutSetDao
        private lateinit var workoutDao: WorkoutDao
        private lateinit var workoutSessionDao: WorkoutSessionDao

        private lateinit var workoutSession: WorkoutSessionEntity
        private lateinit var db: AppDatabase

        @Before
        suspend fun createDb() {
            val context = ApplicationProvider.getApplicationContext<Context>()
            db = Room.inMemoryDatabaseBuilder(
                context, AppDatabase::class.java).build()

            exerciseDao = db.exerciseDao()
            workoutSetDao = db.workoutSetDao()
            workoutDao = db.workoutDao()
            workoutSessionDao = db.workoutSessionDao()

            val exerciseEntity = ExerciseEntity(name = "Bench Press", muscleGroup = "Chest", personalRecord = 135.0)
            exerciseDao.insertAll(exerciseEntity)

            val workoutSessionEntity = WorkoutSessionEntity(date = Instant.now().epochSecond)
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

        @After
        @Throws(IOException::class)
        fun closeDb() {
            db.close()
        }

        @Test
        @Throws(Exception::class)
        suspend fun writeUserAndReadInList() {
            val workoutSessions = workoutSessionDao.getAllWithRelations().toList()
            assertTrue(workoutSessions.isNotEmpty())
        }
    }
}