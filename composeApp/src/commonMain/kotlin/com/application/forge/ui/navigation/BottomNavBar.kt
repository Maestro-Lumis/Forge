package com.application.forge.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.forge.ui.adaptive.LocalForgeSpacing
import com.application.forge.ui.theme.ForgeColors

// Bottom Navigation Bar
@Composable
fun ForgeBottomNavBar(
    currentRoute: String,
    onNavigate: (Screen) -> Unit,
) {
    val sp = LocalForgeSpacing.current

    Column {
        // Верхняя линия разделитель
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(ForgeColors.LazurDark),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ForgeColors.Lazur)
                .padding(
                    horizontal = sp.screenPaddingHorizontal,
                    vertical = 8.dp,
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            bottomNavItems.forEach { item ->
                val isSelected = currentRoute == item.screen.route
                BottomNavItem(
                    item = item,
                    isSelected = isSelected,
                    onClick = { onNavigate(item.screen) },
                )
            }
        }
    }
}

// Отдельный элемент навигации

@Composable
fun BottomNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val labelColor = if (isSelected) ForgeColors.Dawn else ForgeColors.Topaz.copy(alpha = 0.5f)
    val indicatorColor = if (isSelected) ForgeColors.Dawn else ForgeColors.Lazur

    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Индикатор выбранного экрана
        Box(
            modifier = Modifier
                .width(20.dp)
                .height(2.dp)
                .background(indicatorColor),
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Лейбл
        Text(
            text = item.label,
            fontSize = 9.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            letterSpacing = if (isSelected) 1.sp else 0.5.sp,
            color = labelColor,
        )
    }
}