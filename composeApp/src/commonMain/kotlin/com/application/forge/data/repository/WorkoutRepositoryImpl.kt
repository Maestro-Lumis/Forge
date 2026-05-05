package com.application.forge.data.repository

import com.application.forge.domain.model.Exercise
import com.application.forge.domain.model.MuscleStatus
import com.application.forge.domain.model.Workout
import com.application.forge.domain.repository.WorkoutRepository

class WorkoutRepositoryImpl : WorkoutRepository {

    override suspend fun getTodayWorkout(): Workout {
        // TODO: заменить на запрос к базе данных
        return Workout(
            id                  = "workout_tuesday",
            dayName             = "ВТОРНИК",
            muscleGroups        = "Спина + Ноги",
            estimatedDurationMin = 90,
            exercises           = listOf(
                Exercise(
                    id = "ex_01",
                    name = "Гиперэкстензия",
                    muscleGroup = "Поясница",
                    sets = 4,
                    reps = 15,
                    restSeconds = 60,
                    weightKg = null,
                    technique = "Руки скрещены на груди. Поднимай корпус до прямой линии.",
                    youtubeQuery = "hyperextension lower back form",
                ),
                Exercise(
                    id           = "ex_02",
                    name         = "Hammer Strength тяга",
                    muscleGroup  = "Спина",
                    sets         = 4,
                    reps         = 12,
                    restSeconds  = 90,
                    weightKg     = 60f,
                    technique    = "Хват свободный. Тяни локтями назад.",
                    youtubeQuery = "hammer strength iso row technique",
                ),
                Exercise(
                    id           = "ex_03",
                    name         = "Тяга верхнего блока (широкий)",
                    muscleGroup  = "Спина",
                    sets         = 4,
                    reps         = 12,
                    restSeconds  = 90,
                    weightKg     = null,
                    technique    = "Тянешь к верхней части груди. Локти вниз и назад.",
                    youtubeQuery = "lat pulldown wide grip upper chest",
                ),
                Exercise(
                    id           = "ex_04",
                    name         = "Жим ногами",
                    muscleGroup  = "Ноги",
                    sets         = 4,
                    reps         = 15,
                    restSeconds  = 90,
                    weightKg     = null,
                    technique    = "Ноги высоко и широко. Колено не блокируй.",
                    youtubeQuery = "leg press high foot placement",
                ),
            ),
        )
    }

    override suspend fun getMuscleStatuses(): List<MuscleStatus> {
        return listOf(
            MuscleStatus("Спина",  82f, "READY",    null),
            MuscleStatus("Ноги",   60f, "READY",    null),
            MuscleStatus("Грудь",  75f, "UPCOMING", "Чт"),
            MuscleStatus("Плечи",  70f, "UPCOMING", "Чт"),
            MuscleStatus("Бицепс",  5f, "PAUSED",   null),
        )
    }

    override suspend fun getWorkoutHistory(): List<Workout> {
        return emptyList() // TODO: запрос из базы
    }
}