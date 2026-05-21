package com.application.forge.ui.screens.workout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.forge.domain.repository.WorkoutRepository
import com.application.forge.domain.usecase.GetUserProfileUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkoutViewModel(
    private val workoutRepository: WorkoutRepository,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(WorkoutState())
    val state: StateFlow<WorkoutState> = _state.asStateFlow()

    private var workoutTimerJob: Job? = null
    private var restTimerJob: Job? = null

    init {
        // workoutId автоматически берётся из nav-аргумента через SavedStateHandle
        val workoutId: String = savedStateHandle["workoutId"] ?: ""
        loadWorkout(workoutId)
    }

    private fun loadWorkout(workoutId: String) {
        viewModelScope.launch {
            try {
                val workout = workoutRepository.getWorkoutById(workoutId)
                val profile = getUserProfileUseCase()

                if (workout == null) {
                    _state.update { it.copy(isLoading = false, error = "Тренировка не найдена") }
                    return@launch
                }

                val injuryWarningMap = profile.injuries
                    .flatMap { injury ->
                        injury.excludedExercises.map { exName -> exName to injury.warningMessage }
                    }
                    .toMap()

                val exercises = workout.exercises
                    .filterNot { it.isExcludedDueToInjury }
                    .mapIndexed { index, exercise ->
                        exercise.toUiModel(
                            injuryWarning = injuryWarningMap[exercise.name],
                            isFirstActive = index == 0,
                        )
                    }

                _state.update { state ->
                    state.copy(
                        isLoading    = false,
                        workoutTitle = workout.dayName,
                        exercises    = exercises,
                        streakDays   = profile.streakDays,
                    )
                }

                startWorkoutTimer()

            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun startWorkoutTimer() {
        workoutTimerJob?.cancel()
        workoutTimerJob = viewModelScope.launch {
            while (true) {
                delay(1_000)
                _state.update { it.copy(elapsedSeconds = it.elapsedSeconds + 1) }
            }
        }
    }

    fun completeSet(reps: Int?, weight: Float?) {
        val current = _state.value
        val exercise = current.currentExercise ?: return
        val setIndex = current.currentSetIndex

        val updatedSets = exercise.sets.mapIndexed { i, set ->
            when {
                i == setIndex     -> set.copy(
                    actualReps   = reps,
                    actualWeight = weight,
                    status       = SetStatus.DONE,
                )
                i == setIndex + 1 -> set.copy(status = SetStatus.ACTIVE)
                else              -> set
            }
        }

        val updatedExercise = exercise.copy(sets = updatedSets)
        val updatedExercises = current.exercises.toMutableList()
        updatedExercises[current.currentExerciseIndex] = updatedExercise

        val allSetsDone = updatedSets.all { it.status == SetStatus.DONE }
        val newSetIndex = if (allSetsDone) setIndex else setIndex + 1
        val newVolume   = calculateVolume(updatedExercises)

        _state.update { state ->
            state.copy(
                exercises       = updatedExercises,
                currentSetIndex = newSetIndex,
                totalVolumeKg   = newVolume,
            )
        }

        val restSeconds = exercise.restSeconds.takeIf { it > 0 } ?: current.restTimer.totalSeconds
        startRestTimer(restSeconds)
    }

    fun nextExercise() {
        val current = _state.value
        if (current.isLastExercise) {
            _state.update { it.copy(isWorkoutFinished = true) }
            return
        }

        val nextIndex    = current.currentExerciseIndex + 1
        val nextExercise = current.exercises[nextIndex]
        val activatedSets = nextExercise.sets.mapIndexed { i, set ->
            if (i == 0) set.copy(status = SetStatus.ACTIVE) else set
        }
        val updatedExercises = current.exercises.toMutableList()
        updatedExercises[nextIndex] = nextExercise.copy(sets = activatedSets)

        stopRestTimer()

        _state.update { state ->
            state.copy(
                exercises            = updatedExercises,
                currentExerciseIndex = nextIndex,
                currentSetIndex      = 0,
            )
        }
    }

    fun setRestPreset(seconds: Int) {
        stopRestTimer()
        _state.update { state ->
            state.copy(
                restTimer = state.restTimer.copy(
                    totalSeconds     = seconds,
                    remainingSeconds = seconds,
                    isRunning        = false,
                )
            )
        }
    }

    fun startRestTimer(seconds: Int) {
        stopRestTimer()
        _state.update { state ->
            state.copy(
                restTimer = state.restTimer.copy(
                    totalSeconds     = seconds,
                    remainingSeconds = seconds,
                    isRunning        = true,
                )
            )
        }
        restTimerJob = viewModelScope.launch {
            var remaining = seconds
            while (remaining > 0) {
                delay(1_000)
                remaining--
                _state.update { state ->
                    state.copy(restTimer = state.restTimer.copy(remainingSeconds = remaining))
                }
            }
            _state.update { state ->
                state.copy(restTimer = state.restTimer.copy(isRunning = false))
            }
        }
    }

    fun stopRestTimer() {
        restTimerJob?.cancel()
        restTimerJob = null
        _state.update { state ->
            state.copy(restTimer = state.restTimer.copy(isRunning = false))
        }
    }

    fun finishWorkout() {
        _state.update { it.copy(isWorkoutFinished = true) }
    }

    private fun calculateVolume(exercises: List<ExerciseUiModel>): Float {
        return exercises.sumOf { ex ->
            ex.sets
                .filter { it.status == SetStatus.DONE }
                .sumOf { set ->
                    val reps   = set.actualReps ?: 0
                    val weight = set.actualWeight ?: 0f
                    (reps * weight).toDouble()
                }
        }.toFloat()
    }

    override fun onCleared() {
        super.onCleared()
        workoutTimerJob?.cancel()
        restTimerJob?.cancel()
    }
}