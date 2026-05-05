package com.application.forge.domain.model

data class MuscleStatus(
    val name: String,
    val progressPercent: Float,
    val recoveryStatus: String,
    val nextTrainingDay: String?,
)