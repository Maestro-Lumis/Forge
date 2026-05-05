package com.application.forge.domain.repository

import com.application.forge.domain.model.UserProfile

interface UserRepository {
    suspend fun getUserProfile(): UserProfile
    suspend fun updateWeight(weightKg: Float)
    suspend fun updateGoal(goalKg: Float)
}