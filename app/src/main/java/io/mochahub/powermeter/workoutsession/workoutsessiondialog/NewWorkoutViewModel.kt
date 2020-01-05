package io.mochahub.powermeter.workoutsession.workoutsessiondialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.data.ExerciseEntity
import io.mochahub.powermeter.data.WorkoutEntity
import io.mochahub.powermeter.data.WorkoutSessionEntity
import io.mochahub.powermeter.data.WorkoutSetEntity
import io.mochahub.powermeter.models.Workout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class NewWorkoutViewModel(val db: AppDatabase) : ViewModel() {
    val myFormat = "MM/dd/yy"
    val sdf = SimpleDateFormat(myFormat, Locale.US)

    val exercises: LiveData<List<ExerciseEntity>> = db.exerciseDao().getAll()

    fun saveWorkoutSession(name: String, date: String, workouts: List<Workout>) {
        var workoutSessionEntity = WorkoutSessionEntity(name = name, date = sdf.parse(date).time / 1000L)
        var workoutEntities = ArrayList<WorkoutEntity>()
        var workoutSetEntities = ArrayList<WorkoutSetEntity>()

        val exerciseToID = getExerciseMap()
        // TODO: Validate all the data first
        workouts.forEach {
            var exerciseID = exerciseToID.getValue(it.exercise.name)

            var workoutEntity = WorkoutEntity(workoutSessionID = workoutSessionEntity.id, exerciseID = exerciseID)
            workoutEntities.add(workoutEntity)

            it.sets.forEach { workoutSet ->
                var workoutSetEntity = WorkoutSetEntity(
                    workoutSessionID = workoutSessionEntity.id, workoutID = workoutEntity.id,
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

    private fun getExerciseMap(): Map<String, Int> {
        var exercises = db.exerciseDao().getAll().value
        var exerciseToID = HashMap<String, Int>()
        exercises?.forEach {
            exerciseToID[it.name] = it.id
        }
        return exerciseToID
    }
}