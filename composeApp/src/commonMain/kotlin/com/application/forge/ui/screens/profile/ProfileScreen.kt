package com.application.forge.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.application.forge.ui.adaptive.LocalForgeSpacing
import com.application.forge.ui.screens.dashboard.ForgeDivider
import com.application.forge.ui.screens.dashboard.ForgeDividerThin
import com.application.forge.ui.screens.dashboard.SectionLabel
import com.application.forge.ui.theme.ForgeColors
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Загрузка
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ForgeColors.Lazur),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = ForgeColors.Dawn)
        }
        return
    }

    // Ошибка
    state.error?.let { errorMsg ->
        val sp = LocalForgeSpacing.current
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ForgeColors.Lazur),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = errorMsg,
                    color = ForgeColors.Topaz,
                    fontSize = sp.fontSize.bodyLarge.sp,
                )
                Spacer(modifier = Modifier.height(sp.itemSpacing))
                Box(
                    modifier = Modifier
                        .background(ForgeColors.Dawn)
                        .clickable { viewModel.onEvent(ProfileEvent.DismissError) }
                        .padding(
                            horizontal = sp.cardPadding * 2,
                            vertical = sp.cardPadding,
                        ),
                ) {
                    Text(
                        text = "ПОВТОРИТЬ",
                        color = ForgeColors.OnCTA,
                        fontSize = sp.fontSize.labelSmall.sp,
                        letterSpacing = 2.sp,
                    )
                }
            }
        }
        return
    }

    ProfileContent(state = state)
}

@Composable
private fun ProfileContent(state: ProfileState) {
    val sp = LocalForgeSpacing.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ForgeColors.Lazur)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .verticalScroll(rememberScrollState()),
    ) {
        ProfileTopBar()

        ForgeDivider()

        ProfileHero(state = state)

        ForgeDivider()

        SectionLabel(text = "ТЕЛО")
        BodyStatsSection(state = state)

        ForgeDivider()

        SectionLabel(text = "ПРОГРЕСС")
        WeightProgressSection(state = state)

        ForgeDivider()

        SectionLabel(text = "ОГРАНИЧЕНИЯ")
        InjuriesSection(injuries = state.injuries)

        Spacer(modifier = Modifier.height(sp.sectionSpacing))
    }
}

// Шапка
@Composable
private fun ProfileTopBar() {
    val sp = LocalForgeSpacing.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = sp.screenPaddingHorizontal,
                vertical = sp.screenPaddingVertical,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "FORGE",
            fontSize = sp.fontSize.labelSmall.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 3.sp,
            color = ForgeColors.Topaz,
        )
        Text(
            text = "ПРОФИЛЬ",
            fontSize = sp.fontSize.labelSmall.sp,
            letterSpacing = 2.sp,
            color = ForgeColors.Topaz,
        )
    }
}

// Пользователь
@Composable
private fun ProfileHero(state: ProfileState) {
    val sp = LocalForgeSpacing.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = sp.screenPaddingHorizontal,
                vertical = sp.screenPaddingVertical,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            val displayName = state.name.ifBlank { "Атлет" }
            Text(
                text = displayName.uppercase(),
                fontSize = sp.fontSize.headlineLarge.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                color = ForgeColors.Icicle,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${state.age} лет · ${state.heightCm} см",
                fontSize = sp.fontSize.bodyMedium.sp,
                color = ForgeColors.Topaz,
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${state.streakDays}",
                fontSize = sp.fontSize.headlineLarge.sp,
                fontWeight = FontWeight.Black,
                color = ForgeColors.Dawn,
            )
            Text(
                text = "дней подряд",
                fontSize = sp.fontSize.labelSmall.sp,
                color = ForgeColors.Topaz,
                letterSpacing = 1.sp,
            )
        }
    }
}

// Показатели тела
@Composable
private fun BodyStatsSection(state: ProfileState) {
    val sp = LocalForgeSpacing.current

    val bmiLabel = when {
        state.bmi < 18.5f -> "ДЕФИЦИТ"
        state.bmi < 25f   -> "НОРМА"
        state.bmi < 30f   -> "ИЗБЫТОК"
        else              -> "ОЖИРЕНИЕ"
    }
    val bmiColor = when {
        state.bmi < 18.5f -> ForgeColors.Topaz
        state.bmi < 25f   -> Color(0xFF1D9E75)
        state.bmi < 30f   -> ForgeColors.Dawn
        else              -> Color(0xFFE05555)
    }

    // ИМТ
    val bmiInt = (state.bmi * 10).toInt()
    val bmiDisplay = "${bmiInt / 10}.${bmiInt % 10}"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = sp.screenPaddingHorizontal),
        horizontalArrangement = Arrangement.spacedBy(sp.itemSpacing),
    ) {
        BodyStatCard(
            modifier = Modifier.weight(1f),
            value = "${state.weightKg.toInt()} кг",
            label = "ВЕС",
        )
        BodyStatCard(
            modifier = Modifier.weight(1f),
            value = "${state.heightCm} см",
            label = "РОСТ",
        )
        BodyStatCard(
            modifier = Modifier.weight(1f),
            value = bmiDisplay,
            label = bmiLabel,
            valueColor = bmiColor,
        )
    }

    Spacer(modifier = Modifier.height(sp.sectionSpacing))
}

@Composable
private fun BodyStatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    valueColor: Color = ForgeColors.Icicle,
) {
    val sp = LocalForgeSpacing.current

    Column(
        modifier = modifier
            .background(ForgeColors.LazurDark)
            .padding(sp.cardPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = value,
            fontSize = sp.fontSize.headlineMedium.sp,
            fontWeight = FontWeight.Bold,
            color = valueColor,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = sp.fontSize.labelSmall.sp,
            color = ForgeColors.Topaz,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center,
        )
    }
}

// Прогресс веса
@Composable
private fun WeightProgressSection(state: ProfileState) {
    val sp = LocalForgeSpacing.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = sp.screenPaddingHorizontal),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            Column {
                Text(
                    text = "СТАРТ",
                    fontSize = sp.fontSize.labelSmall.sp,
                    color = ForgeColors.Topaz,
                    letterSpacing = 1.sp,
                )
                Text(
                    text = "${state.startWeightKg.toInt()} кг",
                    fontSize = sp.fontSize.bodyLarge.sp,
                    color = ForgeColors.Topaz,
                    fontWeight = FontWeight.Bold,
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "-${state.lostKg.toInt()} кг",
                    fontSize = sp.fontSize.headlineMedium.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1D9E75),
                )
                Text(
                    text = "СБРОШЕНО",
                    fontSize = sp.fontSize.labelSmall.sp,
                    color = ForgeColors.Topaz,
                    letterSpacing = 1.sp,
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "ЦЕЛЬ",
                    fontSize = sp.fontSize.labelSmall.sp,
                    color = ForgeColors.Topaz,
                    letterSpacing = 1.sp,
                )
                Text(
                    text = "${state.goalKg.toInt()} кг",
                    fontSize = sp.fontSize.bodyLarge.sp,
                    color = ForgeColors.Dawn,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Spacer(modifier = Modifier.height(sp.itemSpacing))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(ForgeColors.LazurDark),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(state.progressFraction)
                    .height(4.dp)
                    .background(Color(0xFF1D9E75)),
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Осталось ${state.remainingKg.toInt()} кг до цели",
            fontSize = sp.fontSize.labelSmall.sp,
            color = ForgeColors.Topaz,
        )
    }

    Spacer(modifier = Modifier.height(sp.sectionSpacing))
}

// Секция травм
@Composable
private fun InjuriesSection(injuries: List<InjuryUi>) {
    val sp = LocalForgeSpacing.current

    if (injuries.isEmpty()) {
        Text(
            text = "Нет активных ограничений",
            fontSize = sp.fontSize.bodyMedium.sp,
            color = ForgeColors.Topaz,
            modifier = Modifier.padding(horizontal = sp.screenPaddingHorizontal),
        )
        Spacer(modifier = Modifier.height(sp.sectionSpacing))
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = sp.screenPaddingHorizontal),
    ) {
        injuries.forEachIndexed { index, injury ->
            InjuryRow(injury = injury)
            if (index < injuries.lastIndex) {
                ForgeDividerThin()
            }
        }
    }

    Spacer(modifier = Modifier.height(sp.itemSpacing))
}

@Composable
private fun InjuryRow(injury: InjuryUi) {
    val sp = LocalForgeSpacing.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = sp.itemSpacing),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = injury.bodyPart.uppercase(),
                fontSize = sp.fontSize.bodyLarge.sp,
                fontWeight = FontWeight.Bold,
                color = ForgeColors.Icicle,
            )
            Text(
                text = injury.severity,
                fontSize = sp.fontSize.labelSmall.sp,
                color = ForgeColors.Dawn,
                letterSpacing = 1.sp,
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(16.dp)
                    .background(ForgeColors.Dawn),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = injury.warning,
                fontSize = sp.fontSize.labelSmall.sp,
                color = ForgeColors.Topaz,
                letterSpacing = 1.sp,
            )
        }

        if (injury.excludedExercises.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Исключено: ${injury.excludedExercises.joinToString(", ")}",
                fontSize = sp.fontSize.labelSmall.sp,
                color = ForgeColors.Topaz.copy(alpha = 0.6f),
            )
        }
    }
}