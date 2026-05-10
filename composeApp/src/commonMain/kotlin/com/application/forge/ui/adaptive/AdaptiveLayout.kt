package com.application.forge.ui.adaptive

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Оборачивает весь контент и вычисляет размер экрана
@Composable
fun AdaptiveLayout(
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val widthDp  = maxWidth
        val heightDp = maxHeight

        val windowWidth = when {
            widthDp < 360.dp  -> WindowWidth.Compact
            widthDp < 600.dp  -> WindowWidth.Medium
            widthDp < 840.dp  -> WindowWidth.Large
            else              -> WindowWidth.Expanded
        }

        val windowHeight = when {
            heightDp < 480.dp -> WindowHeight.Compact
            heightDp < 900.dp -> WindowHeight.Medium
            else              -> WindowHeight.Expanded
        }

        val sizeInfo = WindowSizeInfo(
            width    = windowWidth,
            height   = windowHeight,
            widthDp  = widthDp,
            heightDp = heightDp,
        )

        val spacing = windowSizeSpacing(sizeInfo)

        CompositionLocalProvider(
            LocalWindowSizeInfo provides sizeInfo,
            LocalForgeSpacing    provides spacing,
        ) {
            content()
        }
    }
}