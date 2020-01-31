package io.mochahub.powermeter.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.mochahub.powermeter.data.exercise.ExerciseDao
import io.mochahub.powermeter.data.exercise.ExerciseEntity
import io.mochahub.powermeter.data.workout.WorkoutDao
import io.mochahub.powermeter.data.workout.WorkoutEntity
import io.mochahub.powermeter.data.workoutsession.WorkoutSessionDao
import io.mochahub.powermeter.data.workoutsession.WorkoutSessionEntity
import io.mochahub.powermeter.data.workoutset.WorkoutSetDao
import io.mochahub.powermeter.data.workoutset.WorkoutSetEntity

@Database(
    entities = [
        ExerciseEntity::class,
        WorkoutSessionEntity::class,
        WorkoutEntity::class, WorkoutSetEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao

    abstract fun workoutSessionDao(): WorkoutSessionDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutSetDao(): WorkoutSetDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "power-meter.db"
        ).build()
    }
}
