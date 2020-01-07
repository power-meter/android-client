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
import io.mochahub.powermeter.models.Exercise
import io.mochahub.powermeter.models.Workout
import io.mochahub.powermeter.models.WorkoutSession
import io.mochahub.powermeter.models.WorkoutSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Locale

class NewWorkoutViewModel(val db: AppDatabase) : ViewModel() {
    val simpleDateFormat = SimpleDateFormat("MM/dd/yy", Locale.US)

    val exercises: LiveData<List<ExerciseEntity>> = db.exerciseDao().getAll()

    fun getWorkouts(workoutSessionID: String): ArrayList<Workout> {
        val workouts = ArrayList<Workout>()
        val workoutEntities = db.workoutDao().getWorkoutsByWorkoutSession(workoutSessionID)

        workoutEntities.forEach { workoutEntity ->
            val exercise = db.exerciseDao().findByID(workoutEntity.exerciseUUID)
            val workout = Workout(
                exercise = Exercise(
                    name = exercise.name,
                    personalRecord = exercise.personalRecord,
                    muscleGroup = exercise.muscleGroup),
                sets = ArrayList())

            val workoutSets = db.workoutSetDao().getWorkoutSetsByWorkout(workoutEntity.id)
            workoutSets.forEach { workoutSetEntity ->
                val workoutSet = WorkoutSet(
                    weight = workoutSetEntity.weight,
                    reps = workoutSetEntity.reps)
                workout.sets.add(workoutSet)
            }
            workouts.add(workout)
        }
        return workouts
    }
    fun deleteWorkoutSession(workoutSessionID: String) {
        db.workoutSessionDao().deleteByID(workoutSessionID = workoutSessionID)
    }

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