package com.application.forge.ui.screens.workout

import com.application.forge.domain.model.Exercise

// Статус подхода в UI
enum class SetStatus { DONE, ACTIVE, PENDING }

// Один подход с UI-состоянием
data class ExerciseSet(
    val index: Int,
    val targetReps: Int,
    val targetWeight: Float?,
    val actualReps: Int?,
    val actualWeight: Float?,
    val status: SetStatus,
)

// Упражнение с развёрнутыми подходами для UI
data class ExerciseUiModel(
    val id: String,
    val name: String,
    val muscleGroup: String,
    val sets: List<ExerciseSet>,
    val restSeconds: Int,
    val technique: String,
    val isExcludedDueToInjury: Boolean,
    val injuryWarning: String?,
)

// Состояние таймера отдыха
data class RestTimerState(
    val isRunning: Boolean = false,
    val totalSeconds: Int = 90,
    val remainingSeconds: Int = 90,
)

// Главное состояние экрана
data class WorkoutState(
    val isLoading: Boolean = true,
    val workoutTitle: String = "",
    val exercises: List<ExerciseUiModel> = emptyList(),
    val currentExerciseIndex: Int = 0,
    val currentSetIndex: Int = 0,
    val restTimer: RestTimerState = RestTimerState(),
    val elapsedSeconds: Int = 0,
    val totalVolumeKg: Float = 0f,
    val streakDays: Int = 0,
    val isWorkoutFinished: Boolean = false,
    val error: String? = null,
) {
    val currentExercise: ExerciseUiModel?
        get() = exercises.getOrNull(currentExerciseIndex)

    val nextExercise: ExerciseUiModel?
        get() = exercises.getOrNull(currentExerciseIndex + 1)

    val isLastExercise: Boolean
        get() = currentExerciseIndex >= exercises.lastIndex

    val progressFraction: Float
        get() {
            val total = exercises.sumOf { it.sets.size }
            if (total == 0) return 0f
            val done = exercises.sumOf { ex ->
                ex.sets.count { it.status == SetStatus.DONE }
            }
            return done.toFloat() / total.toFloat()
        }
}

// Конвертация domain Exercise в UI ExerciseUiModel
fun Exercise.toUiModel(
    injuryWarning: String?,
    isFirstActive: Boolean,
): ExerciseUiModel {
    val setList = (0 until sets).mapIndexed { i, _ ->
        ExerciseSet(
            index = i,
            targetReps = reps,
            targetWeight = weightKg,
            actualReps = null,
            actualWeight = null,
            status = when {
                isFirstActive && i == 0 -> SetStatus.ACTIVE
                else -> SetStatus.PENDING
            },
        )
    }
    return ExerciseUiModel(
        id = id,
        name = name,
        muscleGroup = muscleGroup,
        sets = setList,
        restSeconds = restSeconds,
        technique = technique,
        isExcludedDueToInjury = isExcludedDueToInjury,
        injuryWarning = injuryWarning,
    )
}