package com.application.forge.domain.model

data class Workout(
    val id: String,
    val dayName: String,
    val muscleGroups: String,
    val exercises: List<Exercise>,
    val estimatedDurationMin: Int,
)

data class Exercise(
    val id: String,
    val name: String,
    val muscleGroup: String,
    val sets: Int,
    val reps: Int,
    val restSeconds: Int,
    val weightKg: Float?,
    val technique: String,
    val youtubeQuery: String,
    val isExcludedDueToInjury: Boolean = false,
)