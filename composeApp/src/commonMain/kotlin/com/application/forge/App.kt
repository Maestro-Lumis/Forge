package com.application.forge

import androidx.compose.runtime.Composable
import com.application.forge.ui.adaptive.AdaptiveLayout
import com.application.forge.ui.navigation.ForgeNavGraph
import com.application.forge.ui.theme.ForgeTheme

@Composable
fun App() {
    ForgeTheme {
        AdaptiveLayout {
            ForgeNavGraph()
        }
    }
}