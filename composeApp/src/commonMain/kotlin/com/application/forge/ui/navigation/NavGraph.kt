package com.application.forge.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.application.forge.ui.screens.dashboard.DashboardScreen
import com.application.forge.ui.screens.dashboard.DashboardViewModel
import com.application.forge.ui.screens.profile.ProfileScreen
import com.application.forge.ui.screens.workout.WorkoutScreen
import com.application.forge.ui.screens.workout.WorkoutSummaryScreen
import com.application.forge.ui.screens.workout.WorkoutViewModel
import com.application.forge.ui.theme.ForgeColors
import org.koin.compose.viewmodel.koinViewModel

// NavGraph
@Composable
fun ForgeNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Dashboard.route,
) {
    // Текущий маршрут для подсветки в BottomNavBar
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Dashboard.route

    // Экраны где показывается нижняя панель
    val showBottomBar = currentRoute in listOf(
        Screen.Dashboard.route,
        Screen.Progress.route,
        Screen.AIChat.route,
        Screen.Profile.route,
    )

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        // Основной контент
        Box(
            modifier = Modifier.weight(1f),
        ) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
            ) {

                // Dashboard
                composable(route = Screen.Dashboard.route) {
                    val viewModel = koinViewModel<DashboardViewModel>()
                    val state by viewModel.state.collectAsState()

                    DashboardScreen(
                        state = state,
                        onEvent = { event ->
                            viewModel.onEvent(event)
                            // Навигация на экран тренировки
                        },
                    )
                }

                // Workout
                composable(route = Screen.Workout.route) {
                    // Экран активной тренировки
                    var isFinished by remember { mutableStateOf(false) }
                    val workoutViewModel: WorkoutViewModel = koinViewModel()
                    val state by workoutViewModel.state.collectAsStateWithLifecycle()

                    if (isFinished) {
                        WorkoutSummaryScreen(
                            state = state,
                            onBack = { navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Dashboard.route) { inclusive = true }
                            }},
                        )
                    } else {
                        WorkoutScreen(
                            onFinished = { isFinished = true },
                            viewModel = workoutViewModel,
                        )
                    }
                }

                // Progress
                composable(route = Screen.Progress.route) {
                    PlaceholderScreen(title = "ПРОГРЕСС")
                }

                // AI Cha
                composable(route = Screen.AIChat.route) {
                    PlaceholderScreen(title = "ИИ ТРЕНЕР")
                }

                // Profile
                composable(route = Screen.Profile.route) {
                    ProfileScreen()
                }

                // Onboarding
                composable(route = Screen.Onboarding.route) {
                    PlaceholderScreen(title = "ОНБОРДИНГ")
                }
            }
        }

        // Нижняя панель навигации
        if (showBottomBar) {
            ForgeBottomNavBar(
                currentRoute = currentRoute,
                onNavigate = { screen ->
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Dashboard.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}

// Placeholder

@Composable
fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ForgeColors.Lazur),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            letterSpacing = 3.sp,
            color = ForgeColors.Topaz,
        )
    }
}