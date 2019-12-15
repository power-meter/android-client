package io.mochahub.powermeter.workoutsession

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.mochahub.powermeter.data.AppDatabase
import io.mochahub.powermeter.data.Exercise

class NewWorkoutViewModel(val db: AppDatabase) : ViewModel() {
    val exercises: LiveData<List<Exercise>> = db.exerciseDao().getAll()
}