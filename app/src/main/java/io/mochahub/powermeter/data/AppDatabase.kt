package io.mochahub.powermeter.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.mochahub.powermeter.data.Exercise.ExerciseDao
import io.mochahub.powermeter.data.Exercise.ExerciseEntity
import io.mochahub.powermeter.data.Workout.WorkoutDao
import io.mochahub.powermeter.data.Workout.WorkoutEntity
import io.mochahub.powermeter.data.WorkoutSession.WorkoutSessionDao
import io.mochahub.powermeter.data.WorkoutSession.WorkoutSessionEntity
import io.mochahub.powermeter.data.WorkoutSet.WorkoutSetDao
import io.mochahub.powermeter.data.WorkoutSet.WorkoutSetEntity

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
