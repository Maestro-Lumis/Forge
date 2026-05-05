package com.application.forge.domain.usecase

import com.application.forge.domain.model.Workout
import com.application.forge.domain.repository.WorkoutRepository
import com.application.forge.domain.repository.UserRepository

class GetTodayWorkoutUseCase(
    private val workoutRepository: WorkoutRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): Workout? {
        val profile = userRepository.getUserProfile()
        val workout = workoutRepository.getTodayWorkout() ?: return null

        // Бизнес-логика: исключаем упражнения под травмы
        val excludedExercises = profile.injuries
            .flatMap { it.excludedExercises }
            .toSet()

        return workout.copy(
            exercises = workout.exercises.map { exercise ->
                exercise.copy(
                    isExcludedDueToInjury = exercise.name in excludedExercises
                )
            }
        )
    }
}