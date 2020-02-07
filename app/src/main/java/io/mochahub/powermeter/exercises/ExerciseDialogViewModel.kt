package io.mochahub.powermeter.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.mochahub.powermeter.data.exercise.ExerciseDao
import io.mochahub.powermeter.data.exercise.ExerciseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseDialogViewModel(
    private val exerciseDao: ExerciseDao
) : ViewModel() {

    @Suppress("UNCHECKED_CAST")
    class ExerciseDialogViewModelFactory(
        private val exerciseDao: ExerciseDao
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ExerciseDialogViewModel(exerciseDao) as T
        }
    }

    fun upsertExercise(id: String, name: String, muscleGroup: String, personalRecord: Double) {
        if (name.isBlank()) {
            return
        }
        val exercise =
            if (id.isNotBlank()) ExerciseEntity(id, name, muscleGroup, personalRecord)
            else ExerciseEntity(name = name, muscleGroup = muscleGroup, personalRecord = personalRecord)

        CoroutineScope(Dispatchers.IO).launch {
            val exerciseFound = exerciseDao.findByName(name)
            // TODO: Inform user that exercise already exists
            if (exerciseFound == null) {
                exerciseDao.upsert(exercise)
            }
        }
    }
}
