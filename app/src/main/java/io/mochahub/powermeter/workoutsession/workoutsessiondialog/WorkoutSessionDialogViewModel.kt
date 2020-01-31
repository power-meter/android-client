package io.mochahub.powermeter.workoutsession.workoutsessiondialog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.room.withTransaction
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.data.Exercise.ExerciseEntity
import io.mochahub.powermeter.data.Workout.WorkoutEntity
import io.mochahub.powermeter.data.Workout.WorkoutWithRelation
import io.mochahub.powermeter.data.WorkoutSession.WorkoutSessionEntity
import io.mochahub.powermeter.data.WorkoutSet.WorkoutSetEntity
import io.mochahub.powermeter.models.Workout
import io.mochahub.powermeter.models.WorkoutSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Locale

class WorkoutSessionDialogViewModel(private val db: AppDatabase) : ViewModelProvider.Factory, ViewModel() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WorkoutSessionDialogViewModel(db) as T
    }

    var workouts = ArrayList<Workout>()

    var isReady = MutableLiveData<Boolean>()

    val simpleDateFormat = SimpleDateFormat("MM/dd/yy", Locale.US)

    fun getExercises(): LiveData<List<ExerciseEntity>> {
        return db.exerciseDao().getAll().asLiveData()
    }

    fun getWorkouts(workoutSessionID: String): LiveData<List<WorkoutWithRelation>> {
        return db.workoutDao().getWorkoutsByWorkoutSession(workoutSessionID).asLiveData()
    }

    fun saveWorkoutSession(
        workoutSession: WorkoutSession,
        workoutSessionToDelete: String?
    ): String? {
        val errorMsg = isWorkoutSessionValid(workoutSession)
        if (errorMsg != null) {
            return errorMsg
        }

        val workoutSessionEntity =
            WorkoutSessionEntity(date = workoutSession.date.epochSecond)
        val workoutEntities = ArrayList<WorkoutEntity>()
        val workoutSetEntities = ArrayList<WorkoutSetEntity>()

        CoroutineScope(Dispatchers.IO).launch {
            workoutSession.workouts.forEach {
                val exercise = db.exerciseDao().findByName(it.exercise.name)
                val workoutEntity =
                    WorkoutEntity(
                        workoutSessionUUID = workoutSessionEntity.id,
                        exerciseUUID = exercise.id
                    )

                workoutEntities.add(workoutEntity)
                it.sets.forEach { workoutSet ->
                    val workoutSetEntity =
                        WorkoutSetEntity(
                            workoutSessionUUID = workoutSessionEntity.id,
                            workoutUUID = workoutEntity.id,
                            reps = workoutSet.reps,
                            weight = workoutSet.weight
                        )
                    workoutSetEntities.add(workoutSetEntity)
                }
            }

            db.withTransaction {
                db.workoutSessionDao().insertAll(workoutSessionEntity)
                db.workoutDao().insertAll(*(workoutEntities.toTypedArray()))
                db.workoutSetDao().insertAll(*(workoutSetEntities.toTypedArray()))
                if (workoutSessionToDelete != null) {
                    db.workoutSessionDao().deleteByID(workoutSessionID = workoutSessionToDelete)
                }
            }
        }
        return null
    }

    private fun isWorkoutSessionValid(workoutSession: WorkoutSession): String? {
        // TODO: Make these errors constants
        if (workoutSession.date.isAfter(Instant.now())) {
            Log.d(this.javaClass.canonicalName, "Workout session date is in the future")
            return "Workout session date is in the future"
        }
        if (workoutSession.workouts.isEmpty()) {
            Log.d(this.javaClass.canonicalName, "Empty workouts")
            return "Empty workouts"
        }
        workoutSession.workouts.forEach {
            if (it.exercise.name.isBlank() || it.exercise.name.isEmpty()) {
                Log.d(this.javaClass.canonicalName, "Empty workout name")
                return "Empty Workout name"
            }
            if (it.sets.isEmpty()) {
                Log.d(this.javaClass.canonicalName, "Empty workoutsets")
                return "Empty Workout Sets"
            }
            it.sets.forEach { workoutSet ->
                if (workoutSet.reps == 0) {
                    Log.d(this.javaClass.canonicalName, "Empty reps")
                    return "Empty reps"
                } else if (workoutSet.weight == 0.0) {
                    Log.d(this.javaClass.canonicalName, "Empty weight")
                    return "Empty weight"
                }
            }
        }
        return null
    }
}