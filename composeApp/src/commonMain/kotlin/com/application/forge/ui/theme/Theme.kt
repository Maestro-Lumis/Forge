package com.application.forge.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Forge Palette
object ForgeColors {
    val Lazur       = Color(0xFF006C84)   // основной фон
    val LazurDark   = Color(0xFF005570)   // тёмный вариант
    val LazurDeep   = Color(0xFF004455)   // самый тёмный
    val Topaz       = Color(0xFF6EB5C0)   // вторичный / подписи
    val Dawn        = Color(0xFFFFCCBB)   // акцент / таймер / CTA
    val Icicle      = Color(0xFFE2E8E4)   // текст / светлый
    val IcicleLight = Color(0xFFD4F0D4)   // светло-зелёный текст
    val Surface     = Color(0xFF004D5F)   // предупреждения / панели
    val OnCTA       = Color(0xFF003344)   // текст на Dawn-кнопке
}

// Material3 Color Scheme
private val ForgeDarkColorScheme = darkColorScheme(
    primary          = ForgeColors.Dawn,
    onPrimary        = ForgeColors.OnCTA,
    secondary        = ForgeColors.Topaz,
    onSecondary      = ForgeColors.LazurDeep,
    background       = ForgeColors.Lazur,
    onBackground     = ForgeColors.Icicle,
    surface          = ForgeColors.LazurDark,
    onSurface        = ForgeColors.Icicle,
    surfaceVariant   = ForgeColors.Surface,
    onSurfaceVariant = ForgeColors.Topaz,
    outline          = ForgeColors.LazurDark,
)

//Typography
private val ForgeTypography = Typography(
    // Большие заголовки - номер упражнения, таймер
    displayLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize   = 57.sp,
        letterSpacing = (-1).sp,
        color = ForgeColors.Dawn,
    ),
    // Названия упражнений
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize   = 22.sp,
        letterSpacing = (-0.5).sp,
        color = ForgeColors.Icicle,
    ),
    // Подзаголовки, метки
    titleSmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize   = 11.sp,
        letterSpacing = 1.5.sp,
        color = ForgeColors.Topaz,
    ),
    // Основной текст
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize   = 13.sp,
        color = ForgeColors.Icicle,
    ),
    // Мелкий текст / лейблы
    labelSmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize   = 10.sp,
        letterSpacing = 1.2.sp,
        color = ForgeColors.Topaz,
    ),
)

// Theme Composable
@Composable
fun ForgeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ForgeDarkColorScheme,
        typography  = ForgeTypography,
        content     = content,
    )
}