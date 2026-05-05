package com.application.forge.domain.usecase

import com.application.forge.domain.model.MuscleStatus
import com.application.forge.domain.repository.WorkoutRepository

class GetMuscleStatusUseCase(
    private val workoutRepository: WorkoutRepository,
) {
    suspend operator fun invoke(): List<MuscleStatus> {
        return workoutRepository.getMuscleStatuses()
    }
}