package com.application.forge.ui.screens.profile

// UI State
data class ProfileState(
    val isLoading: Boolean = true,
    val error: String? = null,

    // Основные данные
    val name: String = "",
    val age: Int = 0,
    val heightCm: Int = 0,

    // Вес
    val weightKg: Float = 0f,
    val startWeightKg: Float = 0f,
    val goalKg: Float = 0f,

    // Прогресс
    val streakDays: Int = 0,

    // Травмы
    val injuries: List<InjuryUi> = emptyList(),
) {
    val lostKg: Float get() = startWeightKg - weightKg
    val remainingKg: Float get() = weightKg - goalKg
    val progressFraction: Float
        get() {
            val total = startWeightKg - goalKg
            if (total <= 0f) return 1f
            return ((startWeightKg - weightKg) / total).coerceIn(0f, 1f)
        }
    val bmi: Float
        get() {
            if (heightCm <= 0) return 0f
            val heightM = heightCm / 100f
            return weightKg / (heightM * heightM)
        }
}

// UI Models
data class InjuryUi(
    val bodyPart: String,
    val severity: String,
    val warning: String,
    val excludedExercises: List<String>,
)

// UI Events
sealed class ProfileEvent {
    data object RefreshData : ProfileEvent()
    data object DismissError : ProfileEvent()
}