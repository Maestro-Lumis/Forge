package com.application.forge.domain.usecase

import com.application.forge.domain.model.Workout
import com.application.forge.domain.repository.WorkoutRepository

class GetAllWorkoutsUseCase(
    private val workoutRepository: WorkoutRepository,
) {
    suspend operator fun invoke(): List<Workout> {
        return workoutRepository.getAllWorkouts()
    }
}