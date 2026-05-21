package com.application.forge.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.application.forge.ui.screens.dashboard.DashboardEvent
import com.application.forge.ui.screens.dashboard.DashboardScreen
import com.application.forge.ui.screens.dashboard.DashboardViewModel
import com.application.forge.ui.screens.profile.ProfileScreen
import com.application.forge.ui.screens.progress.ProgressScreen
import com.application.forge.ui.screens.workout.WorkoutPickerScreen
import com.application.forge.ui.screens.workout.WorkoutScreen
import com.application.forge.ui.screens.workout.WorkoutSummaryScreen
import com.application.forge.ui.screens.workout.WorkoutViewModel
import com.application.forge.ui.theme.ForgeColors
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ForgeNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Dashboard.route,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Dashboard.route

    val showBottomBar = currentRoute in listOf(
        Screen.Dashboard.route,
        Screen.Progress.route,
        Screen.AIChat.route,
        Screen.Profile.route,
    )

    Column(modifier = Modifier.fillMaxSize()) {

        Box(modifier = Modifier.weight(1f)) {
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
                            when (event) {
                                is DashboardEvent.StartWorkout -> {
                                    navController.navigate(Screen.WorkoutPicker.route)
                                }
                                else -> viewModel.onEvent(event)
                            }
                        },
                    )
                }

                // Выбор тренировки
                composable(route = Screen.WorkoutPicker.route) {
                    WorkoutPickerScreen(
                        onWorkoutSelected = { workoutId ->
                            navController.navigate(Screen.Workout.routeWithId(workoutId))
                        },
                        onBack = { navController.popBackStack() },
                    )
                }

                // Активная тренировка
                // workoutId читается автоматически через SavedStateHandle в ViewModel
                composable(
                    route = Screen.Workout.route,
                    arguments = listOf(
                        navArgument("workoutId") { type = NavType.StringType },
                    ),
                ) {
                    var isFinished by remember { mutableStateOf(false) }
                    val workoutViewModel: WorkoutViewModel = koinViewModel()
                    val state by workoutViewModel.state.collectAsStateWithLifecycle()

                    if (isFinished) {
                        WorkoutSummaryScreen(
                            state = state,
                            onBack = {
                                navController.navigate(Screen.Dashboard.route) {
                                    popUpTo(Screen.Dashboard.route) { inclusive = true }
                                }
                            },
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
                    ProgressScreen()
                }

                // AI Chat
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

        if (showBottomBar) {
            ForgeBottomNavBar(
                currentRoute = currentRoute,
                onNavigate = { screen ->
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Dashboard.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
            )
        }
    }
}

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