package com.application.forge.ui.screens.progress

// UI State
data class ProgressState(
    val isLoading: Boolean = true,
    val error: String? = null,

    // Вес
    val currentWeightKg: Float = 0f,
    val startWeightKg: Float = 0f,
    val goalKg: Float = 0f,
    val streakDays: Int = 0,

    // Мышцы
    val muscleStatuses: List<MuscleProgressUi> = emptyList(),
) {
    val lostKg: Float get() = startWeightKg - currentWeightKg
    val remainingKg: Float get() = currentWeightKg - goalKg
    val weightProgressFraction: Float
        get() {
            val total = startWeightKg - goalKg
            if (total <= 0f) return 1f
            return ((startWeightKg - currentWeightKg) / total).coerceIn(0f, 1f)
        }
}

// UI Models
data class MuscleProgressUi(
    val name: String,
    val progressPercent: Int,
    val status: MuscleStatusUi,
    val nextTrainingDay: String?,
)

enum class MuscleStatusUi {
    READY,
    UPCOMING,
    PAUSED,
}

// UI Events
sealed class ProgressEvent {
    data object RefreshData : ProgressEvent()
    data object DismissError : ProgressEvent()
}