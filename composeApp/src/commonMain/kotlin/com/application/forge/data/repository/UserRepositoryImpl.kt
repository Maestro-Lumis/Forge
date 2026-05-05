package com.application.forge.data.repository

import com.application.forge.domain.model.Injury
import com.application.forge.domain.model.InjurySeverity
import com.application.forge.domain.model.UserProfile
import com.application.forge.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {

    override suspend fun getUserProfile(): UserProfile {
        // TODO: заменить на запрос к базе данных / SharedPreferences
        return UserProfile(
            id             = "user_01",
            name           = "",
            weightKg       = 106f,
            goalKg         = 95f,
            startWeightKg  = 114f,
            heightCm       = 193,
            age            = 23,
            streakDays     = 7,
            injuries       = listOf(
                Injury(
                    bodyPart = "Предплечье",
                    severity = InjurySeverity.MODERATE,
                    warningMessage = "ПРЕДПЛЕЧЬЕ — ЛЯМКИ ОБЯЗАТЕЛЬНО",
                    excludedExercises = listOf(
                        "Подъём на бицепс",
                        "Hammer curl",
                        "Подтягивания",
                        "Становая тяга",
                    ),
                ),
                Injury(
                    bodyPart          = "Колени",
                    severity          = InjurySeverity.MILD,
                    warningMessage    = "КОЛЕНИ — НОГИ ВЫСОКО НА ПЛАТФОРМЕ",
                    excludedExercises = listOf(
                        "Приседания",
                        "Выпады",
                    ),
                ),
            ),
        )
    }

    override suspend fun updateWeight(weightKg: Float) {
        // TODO: сохранить в базу
    }

    override suspend fun updateGoal(goalKg: Float) {
        // TODO: сохранить в базу
    }
}