package com.application.forge.ui.navigation

// Screen - все маршруты приложения

sealed class Screen(val route: String) {

    // Онбординг - показывается только при первом запуске
    data object Onboarding : Screen("onboarding")

    // Главная
    data object Dashboard : Screen("dashboard")

    // Активная тренировка
    data object Workout : Screen("workout")

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
        screen = Screen.Workout,
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