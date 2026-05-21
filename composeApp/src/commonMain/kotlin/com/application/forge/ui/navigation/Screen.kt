package com.application.forge.ui.navigation

// Screen
sealed class Screen(val route: String) {

    // Онбординг
    data object Onboarding : Screen("onboarding")

    // Главная
    data object Dashboard : Screen("dashboard")

    // Выбор тренировки перед стартом
    data object WorkoutPicker : Screen("workout_picker")

    // Активная тренировка (принимает workoutId)
    data object Workout : Screen("workout/{workoutId}") {
        fun routeWithId(workoutId: String) = "workout/$workoutId"
    }

    // Прогресс и замеры
    data object Progress : Screen("progress")

    // Чат с ИИ тренером
    data object AIChat : Screen("ai_chat")

    // Профиль и травмы
    data object Profile : Screen("profile")
}

// Экраны нижней панели навигации
data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: String,
    val iconSelected: String,
)

val bottomNavItems = listOf(
    BottomNavItem(
        screen = Screen.Dashboard,
        label = "Главная",
        icon = "⊡",
        iconSelected = "⊞",
    ),
    BottomNavItem(
        screen = Screen.WorkoutPicker,
        label = "Тренировка",
        icon = "◎",
        iconSelected = "●",
    ),
    BottomNavItem(
        screen = Screen.Progress,
        label = "Прогресс",
        icon = "⌇",
        iconSelected = "⌇",
    ),
    BottomNavItem(
        screen = Screen.AIChat,
        label = "Тренер",
        icon = "◻",
        iconSelected = "◼",
    ),
    BottomNavItem(
        screen = Screen.Profile,
        label = "Профиль",
        icon = "○",
        iconSelected = "●",
    ),
)