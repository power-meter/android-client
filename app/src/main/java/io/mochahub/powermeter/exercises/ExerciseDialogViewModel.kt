package io.mochahub.powermeter.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.mochahub.powermeter.data.exercise.ExerciseDao
import io.mochahub.powermeter.data.exercise.ExerciseEntity
import io.mochahub.powermeter.data.exercise.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseDialogViewModel(
    private val exerciseDao: ExerciseDao,
    args: ExerciseDialogArgs
) : ViewModel() {

    @Suppress("UNCHECKED_CAST")
    class ExerciseDialogViewModelFactory(
        private val exerciseDao: ExerciseDao,
        private val args: ExerciseDialogArgs
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ExerciseDialogViewModel(exerciseDao, args) as T
        }
    }

    private var exercise = ExerciseEntity(
        args.exerciseId,
        args.exerciseName,
        args.muscleGroup,
        args.exercisePR.toDouble()
    )

    fun upsertExercise(name: String, muscleGroup: String, personalRecord: Double) {
        exercise = exercise.update(name, muscleGroup, personalRecord)
        if (exercise.name.isBlank()) {
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            exerciseDao.upsert(exercise)
        }
    }

    fun getExercise(): ExerciseEntity {
        return this.exercise.copy()
    }
}
