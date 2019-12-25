package io.mochahub.powermeter.workoutsession.workoutsessiondialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.data.ExerciseEntity

class NewWorkoutViewModel(val db: AppDatabase) : ViewModel() {
    val exercises: LiveData<List<ExerciseEntity>> = db.exerciseDao().getAll()
}