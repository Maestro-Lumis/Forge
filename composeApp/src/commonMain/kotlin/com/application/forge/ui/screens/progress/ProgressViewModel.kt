package com.application.forge.ui.screens.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.forge.domain.usecase.GetMuscleStatusUseCase
import com.application.forge.domain.usecase.GetUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProgressViewModel(
    private val getUserProfile: GetUserProfileUseCase,
    private val getMuscleStatus: GetMuscleStatusUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ProgressState())
    val state: StateFlow<ProgressState> = _state.asStateFlow()

    init {
        loadData()
    }

    fun onEvent(event: ProgressEvent) {
        when (event) {
            is ProgressEvent.RefreshData  -> loadData()
            is ProgressEvent.DismissError -> dismissError()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val profile = getUserProfile()
                val muscles = getMuscleStatus()

                _state.update {
                    it.copy(
                        isLoading        = false,
                        currentWeightKg  = profile.weightKg,
                        startWeightKg    = profile.startWeightKg,
                        goalKg           = profile.goalKg,
                        streakDays       = profile.streakDays,
                        muscleStatuses   = muscles.map { m ->
                            MuscleProgressUi(
                                name             = m.name,
                                progressPercent  = m.progressPercent.toInt(),
                                status           = when (m.recoveryStatus) {
                                    "READY"    -> MuscleStatusUi.READY
                                    "PAUSED"   -> MuscleStatusUi.PAUSED
                                    else       -> MuscleStatusUi.UPCOMING
                                },
                                nextTrainingDay  = m.nextTrainingDay,
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

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}