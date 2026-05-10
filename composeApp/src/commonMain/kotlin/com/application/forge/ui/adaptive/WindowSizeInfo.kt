package com.application.forge.ui.adaptive

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Размеры экрана
enum class WindowWidth {
    Compact,   // < 360dp
    Medium,    // 360–600dp
    Large,     // 600–840dp
    Expanded,  // > 840dp
}

enum class WindowHeight {
    Compact,   // < 480dp  - горизонталь или маленький экран
    Medium,    // 480–900dp - стандарт
    Expanded,  // > 900dp  - большой экран
}

data class WindowSizeInfo(
    val width: WindowWidth,
    val height: WindowHeight,
    val widthDp: Dp,
    val heightDp: Dp,
) {
    val isTablet: Boolean get() = width == WindowWidth.Expanded
    val isCompact: Boolean get() = width == WindowWidth.Compact
    val isLargePhone: Boolean get() = width == WindowWidth.Large
}

// CompositionLocal для доступа везде
val LocalWindowSizeInfo = compositionLocalOf {
    WindowSizeInfo(
        width = WindowWidth.Medium,
        height = WindowHeight.Medium,
        widthDp = 390.dp,
        heightDp = 844.dp,
    )
}

// Адаптивные отступы
data class ForgeSpacing(
    val screenPaddingHorizontal: Dp,
    val screenPaddingVertical: Dp,
    val cardPadding: Dp,
    val itemSpacing: Dp,
    val sectionSpacing: Dp,
    val topBarHeight: Dp,
    val bottomBarHeight: Dp,
    val fontSize: AdaptiveFontSize,
)

data class AdaptiveFontSize(
    val displayLarge: Float,  // номер упражнения
    val headlineLarge: Float, // заголовок тренировки
    val headlineMedium: Float,
    val bodyLarge: Float,
    val bodyMedium: Float,
    val labelSmall: Float,
)

// Значения для каждого размера
fun windowSizeSpacing(info: WindowSizeInfo): ForgeSpacing = when (info.width) {

    WindowWidth.Compact -> ForgeSpacing(
        screenPaddingHorizontal = 12.dp,
        screenPaddingVertical   = 8.dp,
        cardPadding             = 10.dp,
        itemSpacing             = 6.dp,
        sectionSpacing          = 12.dp,
        topBarHeight            = 48.dp,
        bottomBarHeight         = 56.dp,
        fontSize = AdaptiveFontSize(
            displayLarge   = 64f,
            headlineLarge  = 20f,
            headlineMedium = 16f,
            bodyLarge      = 13f,
            bodyMedium     = 11f,
            labelSmall     = 9f,
        ),
    )

    WindowWidth.Medium -> ForgeSpacing(
        screenPaddingHorizontal = 20.dp,
        screenPaddingVertical   = 12.dp,
        cardPadding             = 14.dp,
        itemSpacing             = 8.dp,
        sectionSpacing          = 16.dp,
        topBarHeight            = 56.dp,
        bottomBarHeight         = 64.dp,
        fontSize = AdaptiveFontSize(
            displayLarge   = 80f,
            headlineLarge  = 24f,
            headlineMedium = 20f,
            bodyLarge      = 14f,
            bodyMedium     = 12f,
            labelSmall     = 10f,
        ),
    )

    WindowWidth.Large -> ForgeSpacing(
        screenPaddingHorizontal = 28.dp,
        screenPaddingVertical   = 16.dp,
        cardPadding             = 16.dp,
        itemSpacing             = 10.dp,
        sectionSpacing          = 20.dp,
        topBarHeight            = 60.dp,
        bottomBarHeight         = 68.dp,
        fontSize = AdaptiveFontSize(
            displayLarge   = 90f,
            headlineLarge  = 28f,
            headlineMedium = 22f,
            bodyLarge      = 15f,
            bodyMedium     = 13f,
            labelSmall     = 11f,
        ),
    )

    WindowWidth.Expanded -> ForgeSpacing(
        screenPaddingHorizontal = 48.dp,
        screenPaddingVertical   = 24.dp,
        cardPadding             = 20.dp,
        itemSpacing             = 12.dp,
        sectionSpacing          = 24.dp,
        topBarHeight            = 64.dp,
        bottomBarHeight         = 72.dp,
        fontSize = AdaptiveFontSize(
            displayLarge   = 100f,
            headlineLarge  = 32f,
            headlineMedium = 26f,
            bodyLarge      = 16f,
            bodyMedium     = 14f,
            labelSmall     = 12f,
        ),
    )
}

val LocalForgeSpacing = compositionLocalOf { windowSizeSpacing(
    WindowSizeInfo(WindowWidth.Medium, WindowHeight.Medium, 390.dp, 844.dp)
) }