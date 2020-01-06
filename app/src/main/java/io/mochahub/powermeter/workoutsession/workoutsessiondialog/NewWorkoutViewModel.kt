package io.mochahub.powermeter.workoutsession.workoutsessiondialog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.data.ExerciseEntity
import io.mochahub.powermeter.data.WorkoutEntity
import io.mochahub.powermeter.data.WorkoutSessionEntity
import io.mochahub.powermeter.data.WorkoutSetEntity
import io.mochahub.powermeter.models.WorkoutSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Locale

class NewWorkoutViewModel(val db: AppDatabase) : ViewModel() {
    val simpleDateFormat = SimpleDateFormat("MM/dd/yy", Locale.US)

    val exercises: LiveData<List<ExerciseEntity>> = db.exerciseDao().getAll()

    suspend fun saveWorkoutSession(workoutSession: WorkoutSession) {
        if (!isWorkoutSessionValid(workoutSession)) {
            // TODO: Provide descriptive feedback on what was wrong
            return
        }
        val workoutSessionEntity = WorkoutSessionEntity(name = workoutSession.name, date = workoutSession.date.epochSecond)
        val workoutEntities = ArrayList<WorkoutEntity>()
        val workoutSetEntities = ArrayList<WorkoutSetEntity>()

        workoutSession.workouts.forEach {
            val exercise = db.exerciseDao().findByName(it.exercise.name)
            val workoutEntity = WorkoutEntity(workoutSessionUUID = workoutSessionEntity.id, exerciseUUID = exercise.id)

            workoutEntities.add(workoutEntity)
            it.sets.forEach { workoutSet ->
                val workoutSetEntity = WorkoutSetEntity(
                    workoutSessionUUID = workoutSessionEntity.id, workoutUUID = workoutEntity.id,
                    reps = workoutSet.reps, weight = workoutSet.weight)
                workoutSetEntities.add(workoutSetEntity)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            db.withTransaction {
                db.workoutSessionDao().insertAll(workoutSessionEntity)
                db.workoutDao().insertAll(*(workoutEntities.toTypedArray()))
                db.workoutSetDao().insertAll(*(workoutSetEntities.toTypedArray()))
            }
        }
    }

    private fun isWorkoutSessionValid(workoutSession: WorkoutSession): Boolean {
        if (workoutSession.date.isAfter(Instant.now())) {
            Log.d(this.javaClass.canonicalName, "Workout session date is in the future")
            return false
        }

        workoutSession.workouts.forEach {
            if (it.exercise.name.isBlank() || it.exercise.name.isEmpty()) {
                Log.d(this.javaClass.canonicalName, "Empty workout name")
                return false
            }

            it.sets.forEach { workoutSet ->
                if (workoutSet.reps == 0) {
                    Log.d(this.javaClass.canonicalName, "Empty reps")
                    return false
                } else if (workoutSet.weight == 0.0) {
                    Log.d(this.javaClass.canonicalName, "Empty weight")
                    return false
                }
            }
        }
        return true
    }
}