package com.application.forge.di

import com.application.forge.data.repository.UserRepositoryImpl
import com.application.forge.data.repository.WorkoutRepositoryImpl
import com.application.forge.domain.repository.UserRepository
import com.application.forge.domain.repository.WorkoutRepository
import com.application.forge.domain.usecase.GetMuscleStatusUseCase
import com.application.forge.domain.usecase.GetTodayWorkoutUseCase
import com.application.forge.domain.usecase.GetUserProfileUseCase
import com.application.forge.ui.screens.dashboard.DashboardViewModel
import com.application.forge.ui.screens.workout.WorkoutViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    // Data Layer
    // Репозитории — одиночки

    single<WorkoutRepository> { WorkoutRepositoryImpl() }
    single<UserRepository>    { UserRepositoryImpl() }

    // Domain Layer

    factory { GetTodayWorkoutUseCase(get(), get()) }
    factory { GetMuscleStatusUseCase(get()) }
    factory { GetUserProfileUseCase(get()) }

    // UI Layer
    // ViewModel — живёт пока жив экран

    viewModelOf(::DashboardViewModel)

    viewModelOf(::WorkoutViewModel)
}