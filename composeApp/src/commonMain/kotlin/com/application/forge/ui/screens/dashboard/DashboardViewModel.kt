package com.application.forge.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.forge.domain.usecase.GetMuscleStatusUseCase
import com.application.forge.domain.usecase.GetTodayWorkoutUseCase
import com.application.forge.domain.usecase.GetUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val getTodayWorkout: GetTodayWorkoutUseCase,
    private val getMuscleStatus: GetMuscleStatusUseCase,
    private val getUserProfile: GetUserProfileUseCase,
) : ViewModel() {

    // State
    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    // Init
    init {
        loadData()
    }

    // Events
    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.StartWorkout  -> handleStartWorkout()
            is DashboardEvent.RefreshData   -> loadData()
            is DashboardEvent.DismissError  -> dismissError()
        }
    }

    // Private
    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                // Загружаем всё параллельно
                val profile = getUserProfile()
                val workout = getTodayWorkout()
                val muscles = getMuscleStatus()

                _state.update { current ->
                    current.copy(
                        isLoading = false,

                        // Профиль
                        userName   = profile.name,
                        streakDays = profile.streakDays,
                        weightKg   = profile.weightKg,
                        goalKg     = profile.goalKg,
                        startKg    = profile.startWeightKg,

                        // Тренировка
                        todayWorkout = workout?.let {
                            TodayWorkoutUi(
                                dayName       = it.dayName,
                                muscleGroups  = it.muscleGroups,
                                exerciseCount = it.exercises.size,
                                durationMin   = it.estimatedDurationMin,
                            )
                        },

                        // Мышцы
                        muscleStatuses = muscles.map { m ->
                            MuscleStatusUi(
                                name     = m.name,
                                progress = m.progressPercent / 100f,
                                status   = when (m.recoveryStatus) {
                                    "READY"    -> RecoveryStatusUi.READY
                                    "PAUSED"   -> RecoveryStatusUi.PAUSED
                                    else       -> RecoveryStatusUi.UPCOMING
                                },
                            )
                        },

                        // Предупреждения о травмах
                        injuries = profile.injuries.map { injury ->
                            InjuryWarningUi(
                                bodyPart = injury.bodyPart,
                                warning  = injury.warningMessage,
                            )
                        },
                    )
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error     = e.message ?: "Ошибка загрузки данных",
                    )
                }
            }
        }
    }

    private fun handleStartWorkout() {
        // Навигация обрабатывается через SharedFlow или NavController
    }

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}