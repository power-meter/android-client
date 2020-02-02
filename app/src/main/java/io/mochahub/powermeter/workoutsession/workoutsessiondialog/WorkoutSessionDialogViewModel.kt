package io.mochahub.powermeter.workoutsession.workoutsessiondialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.room.withTransaction
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.data.exercise.ExerciseEntity
import io.mochahub.powermeter.data.workout.WorkoutEntity
import io.mochahub.powermeter.data.workout.WorkoutWithRelation
import io.mochahub.powermeter.data.workoutsession.WorkoutSessionEntity
import io.mochahub.powermeter.data.workoutset.WorkoutSetEntity
import io.mochahub.powermeter.models.Workout
import io.mochahub.powermeter.models.WorkoutSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutSessionDialogViewModel(private val db: AppDatabase) : ViewModelProvider.Factory, ViewModel() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WorkoutSessionDialogViewModel(db) as T
    }

    var workoutSession = WorkoutSession(workouts = ArrayList<Workout>())
    var isReady = MutableLiveData<Boolean>()

    fun getExercises(): LiveData<List<ExerciseEntity>> {
        return db.exerciseDao().getAll().asLiveData()
    }

    fun getWorkouts(workoutSessionID: String): LiveData<List<WorkoutWithRelation>> {
        return db.workoutDao().getWorkoutsByWorkoutSession(workoutSessionID).asLiveData()
    }

    fun saveWorkoutSession(
        workoutSessionToDelete: String?
    ) {

        val workoutSessionEntity =
            WorkoutSessionEntity(date = workoutSession.date.epochSecond)
        val workoutEntities = ArrayList<WorkoutEntity>()
        val workoutSetEntities = ArrayList<WorkoutSetEntity>()

        CoroutineScope(Dispatchers.IO).launch {
            workoutSession.workouts.forEach { workout ->
                if (workout.exercise.name.isBlank()) {
                    return@forEach
                }
                val exercise = db.exerciseDao().findByName(workout.exercise.name)
                val workoutEntity =
                    WorkoutEntity(
                        workoutSessionUUID = workoutSessionEntity.id,
                        exerciseUUID = exercise.id
                    )

                workoutEntities.add(workoutEntity)
                workout.sets.forEach { workoutSet ->
                    if (workoutSet.weight != 0.0 && workoutSet.reps != 0) {
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
    }
}