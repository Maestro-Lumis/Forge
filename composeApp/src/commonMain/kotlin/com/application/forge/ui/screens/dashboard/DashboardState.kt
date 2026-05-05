package com.application.forge.ui.screens.dashboard

// ─UI State
data class DashboardState(
    val isLoading: Boolean = true,

    // Пользователь
    val userName: String = "",
    val streakDays: Int = 0,

    // Сегодняшняя тренировка
    val todayWorkout: TodayWorkoutUi? = null,

    // Статистика веса
    val weightKg: Float = 0f,
    val goalKg: Float = 0f,
    val startKg: Float = 0f,

    // Мышцы
    val muscleStatuses: List<MuscleStatusUi> = emptyList(),

    // Предупреждения о травмах
    val injuries: List<InjuryWarningUi> = emptyList(),

    // Ошибка
    val error: String? = null,
)

// UI Models
// Простые data class только для отображения

data class TodayWorkoutUi(
    val dayName: String,
    val muscleGroups: String,
    val exerciseCount: Int,
    val durationMin: Int,
)

data class MuscleStatusUi(
    val name: String,
    val progress: Float,
    val status: RecoveryStatusUi,
)

enum class RecoveryStatusUi {
    READY,
    UPCOMING,
    PAUSED,
}

data class InjuryWarningUi(
    val bodyPart: String,
    val warning: String,
)

// UI Events
// Действия пользователя ViewModel

sealed class DashboardEvent {
    data object StartWorkout : DashboardEvent()
    data object RefreshData : DashboardEvent()
    data object DismissError : DashboardEvent()
}