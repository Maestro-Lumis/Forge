package com.application.forge.domain.model

data class UserProfile(
    val id: String,
    val name: String,
    val weightKg: Float,
    val goalKg: Float,
    val startWeightKg: Float,
    val heightCm: Int,
    val age: Int,
    val streakDays: Int,
    val injuries: List<Injury>,
)

data class Injury(
    val bodyPart: String,
    val severity: InjurySeverity,
    val warningMessage: String,
    val excludedExercises: List<String>,
)

enum class InjurySeverity { MILD, MODERATE, SEVERE }

