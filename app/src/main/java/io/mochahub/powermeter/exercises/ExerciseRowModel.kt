package io.mochahub.powermeter.exercises

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.card.MaterialCardView
import io.mochahub.powermeter.R
import io.mochahub.powermeter.data.ExerciseEntity
import io.mochahub.powermeter.shared.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.row_exercise)
abstract class ExerciseRowModel(
    @EpoxyAttribute var exercise: ExerciseEntity,
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var clickListener: (ExerciseEntity) -> Unit
) : EpoxyModelWithHolder<ExerciseRowModel.Holder>() {

    override fun bind(holder: Holder) {
        holder.nameView.text = exercise.name
        holder.personalRecordView.text = exercise.personalRecord.toString()
        holder.muscleView.text = exercise.muscleGroup
        holder.containerView.setOnClickListener { clickListener(exercise) }
    }

    class Holder : KotlinEpoxyHolder() {
        val containerView by bind<MaterialCardView>(R.id.exerciseRowContainer)
        val nameView by bind<TextView>(R.id.nameTextView)
        val personalRecordView by bind<TextView>(R.id.personalRecordTextView)
        val muscleView by bind<TextView>(R.id.muslceTextView)
    }
}
