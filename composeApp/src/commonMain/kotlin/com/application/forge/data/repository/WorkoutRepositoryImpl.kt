package com.application.forge.data.repository

import com.application.forge.domain.model.Exercise
import com.application.forge.domain.model.MuscleStatus
import com.application.forge.domain.model.Workout
import com.application.forge.domain.repository.WorkoutRepository

class WorkoutRepositoryImpl : WorkoutRepository {

    // Все тренировки плана
    private val allWorkouts = listOf(

        Workout(
            id                   = "workout_tuesday",
            dayName              = "ВТОРНИК",
            muscleGroups         = "Спина + Ноги",
            estimatedDurationMin = 90,
            exercises            = listOf(
                Exercise(
                    id           = "ex_01",
                    name         = "Гиперэкстензия",
                    muscleGroup  = "Поясница",
                    sets         = 4,
                    reps         = 15,
                    restSeconds  = 60,
                    weightKg     = null,
                    technique    = "Руки скрещены на груди. Поднимай корпус до прямой линии.",
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
        ),

        Workout(
            id                   = "workout_thursday",
            dayName              = "ЧЕТВЕРГ",
            muscleGroups         = "Грудь + Плечи + Трицепс",
            estimatedDurationMin = 90,
            exercises            = listOf(
                Exercise(
                    id           = "ex_11",
                    name         = "Жим гантелей лёжа",
                    muscleGroup  = "Грудь",
                    sets         = 4,
                    reps         = 12,
                    restSeconds  = 90,
                    weightKg     = 30f,
                    technique    = "Локти 45°. Опускай до касания груди.",
                    youtubeQuery = "dumbbell bench press form",
                ),
                Exercise(
                    id           = "ex_12",
                    name         = "Разводка гантелей",
                    muscleGroup  = "Грудь",
                    sets         = 3,
                    reps         = 15,
                    restSeconds  = 60,
                    weightKg     = 16f,
                    technique    = "Лёгкий изгиб в локтях. Опускай широко.",
                    youtubeQuery = "dumbbell fly chest technique",
                ),
                Exercise(
                    id           = "ex_13",
                    name         = "Жим гантелей сидя",
                    muscleGroup  = "Плечи",
                    sets         = 4,
                    reps         = 12,
                    restSeconds  = 90,
                    weightKg     = 20f,
                    technique    = "Спина прямая. Не запрокидывай голову.",
                    youtubeQuery = "seated dumbbell shoulder press",
                ),
                Exercise(
                    id           = "ex_14",
                    name         = "Тяга к подбородку",
                    muscleGroup  = "Плечи",
                    sets         = 3,
                    reps         = 15,
                    restSeconds  = 60,
                    weightKg     = null,
                    technique    = "Локти выше кистей. Не тяни до горла.",
                    youtubeQuery = "upright row shoulder form",
                ),
                Exercise(
                    id           = "ex_15",
                    name         = "Французский жим",
                    muscleGroup  = "Трицепс",
                    sets         = 3,
                    reps         = 12,
                    restSeconds  = 60,
                    weightKg     = null,
                    technique    = "Локти фиксированы. Опускай штангу ко лбу.",
                    youtubeQuery = "skull crushers tricep form",
                ),
                Exercise(
                    id           = "ex_16",
                    name         = "Пресс — скручивания",
                    muscleGroup  = "Пресс",
                    sets         = 3,
                    reps         = 20,
                    restSeconds  = 45,
                    weightKg     = null,
                    technique    = "Поясница прижата. Не тяни за голову.",
                    youtubeQuery = "crunches abs form",
                ),
            ),
        ),

        Workout(
            id                   = "workout_saturday",
            dayName              = "СУББОТА",
            muscleGroups         = "Фулл боди (лёгкий)",
            estimatedDurationMin = 60,
            exercises            = listOf(
                Exercise(
                    id           = "ex_21",
                    name         = "Гиперэкстензия",
                    muscleGroup  = "Поясница",
                    sets         = 3,
                    reps         = 15,
                    restSeconds  = 60,
                    weightKg     = null,
                    technique    = "Руки скрещены на груди. Плавный темп.",
                    youtubeQuery = "hyperextension lower back form",
                ),
                Exercise(
                    id           = "ex_22",
                    name         = "Тяга верхнего блока (узкий)",
                    muscleGroup  = "Спина",
                    sets         = 3,
                    reps         = 12,
                    restSeconds  = 60,
                    weightKg     = null,
                    technique    = "Узкий нейтральный хват. Тяни к груди.",
                    youtubeQuery = "close grip lat pulldown",
                ),
                Exercise(
                    id           = "ex_23",
                    name         = "Жим гантелей лёжа",
                    muscleGroup  = "Грудь",
                    sets         = 3,
                    reps         = 12,
                    restSeconds  = 60,
                    weightKg     = 24f,
                    technique    = "70% от рабочего веса. Акцент на технику.",
                    youtubeQuery = "dumbbell bench press form",
                ),
                Exercise(
                    id           = "ex_24",
                    name         = "Жим ногами",
                    muscleGroup  = "Ноги",
                    sets         = 3,
                    reps         = 15,
                    restSeconds  = 60,
                    weightKg     = null,
                    technique    = "Ноги высоко. 70% веса от вторника.",
                    youtubeQuery = "leg press high foot placement",
                ),
                Exercise(
                    id           = "ex_25",
                    name         = "Пресс — планка",
                    muscleGroup  = "Пресс",
                    sets         = 3,
                    reps         = 1,
                    restSeconds  = 45,
                    weightKg     = null,
                    technique    = "60 секунд. Таз не поднимай.",
                    youtubeQuery = "plank form technique",
                ),
            ),
        ),
    )

    override suspend fun getTodayWorkout(): Workout? {
        // TODO: заменить на логику по дню недели
        return allWorkouts.first()
    }

    override suspend fun getAllWorkouts(): List<Workout> {
        return allWorkouts
    }

    override suspend fun getWorkoutById(id: String): Workout? {
        return allWorkouts.find { it.id == id }
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
        return emptyList()
    }
}