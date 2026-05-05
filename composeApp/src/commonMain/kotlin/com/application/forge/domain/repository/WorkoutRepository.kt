package com.application.forge.domain.repository

import com.application.forge.domain.model.Workout
import com.application.forge.domain.model.MuscleStatus

interface WorkoutRepository {
    suspend fun getTodayWorkout(): Workout?
    suspend fun getMuscleStatuses(): List<MuscleStatus>
    suspend fun getWorkoutHistory(): List<Workout>
}