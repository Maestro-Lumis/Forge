package com.application.forge.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen(
    state: DashboardState,
    onEvent: (DashboardEvent) -> Unit,
) {
    // Загрузка
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ForgeColors.Lazur),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = ForgeColors.Dawn)
        }
        return
    }

    // Ошибка
    state.error?.let { errorMsg ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ForgeColors.Lazur),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(errorMsg, color = ForgeColors.Topaz)
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .background(ForgeColors.Dawn)
                        .clickable { onEvent(DashboardEvent.DismissError) }
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text  = "ПОВТОРИТЬ",
                        color = ForgeColors.OnCTA,
                        fontSize = 11.sp,
                        letterSpacing = 2.sp,
                    )
                }
            }
        }
        return
    }

    // Основной контент — передаём state вместо хардкода
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ForgeColors.Lazur)
            .verticalScroll(rememberScrollState()),
    ) {
        ForgeTopBar(streak = state.streakDays)

        state.todayWorkout?.let { workout ->
            HeroWorkoutCard(
                workout = workout,
                injuries = state.injuries,
                onStart = { onEvent(DashboardEvent.StartWorkout) },
            )
        }

        ForgeDivider()

        StatsRow(
            weightKg = state.weightKg,
            goalKg   = state.goalKg,
            startKg  = state.startKg,
        )

        ForgeDivider()

        SectionLabel("ВОССТАНОВЛЕНИЕ")
        RecoverySection(muscles = state.muscleStatuses)

        ForgeDivider()

        SectionLabel("МЫШЕЧНЫЙ БАЛАНС")
        MuscleBalanceSection(muscles = state.muscleStatuses)

        Spacer(Modifier.height(24.dp))
    }
}