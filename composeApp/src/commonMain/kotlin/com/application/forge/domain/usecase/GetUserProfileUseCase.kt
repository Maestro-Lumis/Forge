package com.application.forge.domain.usecase

import com.application.forge.domain.model.UserProfile
import com.application.forge.domain.repository.UserRepository

class GetUserProfileUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): UserProfile {
        return userRepository.getUserProfile()
    }
}