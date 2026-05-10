package com.application.forge

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.application.forge.ui.adaptive.AdaptiveLayout
import com.application.forge.ui.screens.dashboard.DashboardScreen
import com.application.forge.ui.screens.dashboard.DashboardViewModel
import com.application.forge.ui.theme.ForgeTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    ForgeTheme {
        AdaptiveLayout {
            val viewModel = koinViewModel<DashboardViewModel>()
            val state by viewModel.state.collectAsState()
            DashboardScreen(
                state   = state,
                onEvent = viewModel::onEvent,
            )
        }
    }
}