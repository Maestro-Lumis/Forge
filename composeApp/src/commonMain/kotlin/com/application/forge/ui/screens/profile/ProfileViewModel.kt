package com.application.forge.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.forge.domain.model.InjurySeverity
import com.application.forge.domain.usecase.GetUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserProfile: GetUserProfileUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        loadData()
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.RefreshData  -> loadData()
            is ProfileEvent.DismissError -> dismissError()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val profile = getUserProfile()

                _state.update {
                    it.copy(
                        isLoading     = false,
                        name          = profile.name,
                        age           = profile.age,
                        heightCm      = profile.heightCm,
                        weightKg      = profile.weightKg,
                        startWeightKg = profile.startWeightKg,
                        goalKg        = profile.goalKg,
                        streakDays    = profile.streakDays,
                        injuries      = profile.injuries.map { injury ->
                            InjuryUi(
                                bodyPart          = injury.bodyPart,
                                severity          = severityLabel(injury.severity),
                                warning           = injury.warningMessage,
                                excludedExercises = injury.excludedExercises,
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

    private fun severityLabel(severity: InjurySeverity): String = when (severity) {
        InjurySeverity.MILD     -> "СЛАБАЯ"
        InjurySeverity.MODERATE -> "УМЕРЕННАЯ"
        InjurySeverity.SEVERE   -> "СЕРЬЁЗНАЯ"
    }

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}