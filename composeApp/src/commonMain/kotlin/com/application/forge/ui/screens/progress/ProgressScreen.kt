package com.application.forge.ui.screens.progress

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
import androidx.compose.ui.draw.alpha
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

private val Green = Color(0xFF1D9E75)

@Composable
fun ProgressScreen(
    viewModel: ProgressViewModel = koinViewModel(),
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
                        .clickable { viewModel.onEvent(ProgressEvent.DismissError) }
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

    ProgressContent(state = state)
}

@Composable
private fun ProgressContent(state: ProgressState) {
    val sp = LocalForgeSpacing.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ForgeColors.Lazur)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .verticalScroll(rememberScrollState()),
    ) {
        ProgressTopBar()

        ForgeDivider()

        // Ключевые метрики
        SectionLabel(text = "РЕЗУЛЬТАТ")
        ResultMetricsSection(state = state)

        ForgeDivider()

        // Прогресс по весу
        SectionLabel(text = "ВЕС")
        WeightSection(state = state)

        ForgeDivider()

        // Восстановление мышц
        SectionLabel(text = "ВОССТАНОВЛЕНИЕ")
        RecoverySection(muscles = state.muscleStatuses)

        ForgeDivider()

        // Мышечный баланс
        SectionLabel(text = "МЫШЕЧНЫЙ БАЛАНС")
        MuscleBalanceSection(muscles = state.muscleStatuses)

        Spacer(modifier = Modifier.height(sp.sectionSpacing))
    }
}

// Шапка
@Composable
private fun ProgressTopBar() {
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
            text = "ПРОГРЕСС",
            fontSize = sp.fontSize.labelSmall.sp,
            letterSpacing = 2.sp,
            color = ForgeColors.Topaz,
        )
    }
}

// Ключевые метрики серия, сброшено, осталось
@Composable
private fun ResultMetricsSection(state: ProgressState) {
    val sp = LocalForgeSpacing.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = sp.screenPaddingHorizontal),
        horizontalArrangement = Arrangement.spacedBy(sp.itemSpacing),
    ) {
        ResultCard(
            modifier = Modifier.weight(1f),
            value = "${state.streakDays}",
            unit = "дней",
            label = "СЕРИЯ",
            valueColor = ForgeColors.Dawn,
        )
        ResultCard(
            modifier = Modifier.weight(1f),
            value = "${state.lostKg.toInt()}",
            unit = "кг",
            label = "СБРОШЕНО",
            valueColor = Green,
        )
        ResultCard(
            modifier = Modifier.weight(1f),
            value = "${state.remainingKg.toInt()}",
            unit = "кг",
            label = "ОСТАЛОСЬ",
            valueColor = ForgeColors.Icicle,
        )
    }

    Spacer(modifier = Modifier.height(sp.sectionSpacing))
}

@Composable
private fun ResultCard(
    modifier: Modifier = Modifier,
    value: String,
    unit: String,
    label: String,
    valueColor: Color,
) {
    val sp = LocalForgeSpacing.current

    Column(
        modifier = modifier
            .background(ForgeColors.LazurDark)
            .padding(sp.cardPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = value,
                fontSize = sp.fontSize.headlineLarge.sp,
                fontWeight = FontWeight.Black,
                color = valueColor,
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = unit,
                fontSize = sp.fontSize.labelSmall.sp,
                color = valueColor.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 3.dp),
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = sp.fontSize.labelSmall.sp,
            color = ForgeColors.Topaz,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center,
        )
    }
}

// Прогресс веса с баром
@Composable
private fun WeightSection(state: ProgressState) {
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
                    fontWeight = FontWeight.Bold,
                    color = ForgeColors.Topaz,
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${state.currentWeightKg.toInt()} кг",
                    fontSize = sp.fontSize.headlineLarge.sp,
                    fontWeight = FontWeight.Black,
                    color = ForgeColors.Icicle,
                )
                Text(
                    text = "СЕЙЧАС",
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
                    fontWeight = FontWeight.Bold,
                    color = ForgeColors.Dawn,
                )
            }
        }

        Spacer(modifier = Modifier.height(sp.itemSpacing))

        // Прогресс бар
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(ForgeColors.LazurDark),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(state.weightProgressFraction)
                    .height(4.dp)
                    .background(Green),
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        val pct = (state.weightProgressFraction * 100).toInt()
        Text(
            text = "$pct% пути к цели",
            fontSize = sp.fontSize.labelSmall.sp,
            color = ForgeColors.Topaz,
        )
    }

    Spacer(modifier = Modifier.height(sp.sectionSpacing))
}

// Восстановление мышц
@Composable
private fun RecoverySection(muscles: List<MuscleProgressUi>) {
    val sp = LocalForgeSpacing.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = sp.screenPaddingHorizontal),
    ) {
        muscles.forEach { muscle ->
            RecoveryRow(muscle = muscle)
            ForgeDividerThin()
        }
    }

    Spacer(modifier = Modifier.height(sp.itemSpacing))
}

@Composable
private fun RecoveryRow(muscle: MuscleProgressUi) {
    val sp = LocalForgeSpacing.current

    val statusText = when (muscle.status) {
        MuscleStatusUi.READY    -> "Готова ✓"
        MuscleStatusUi.PAUSED   -> "На паузе"
        MuscleStatusUi.UPCOMING -> "Скоро · ${muscle.nextTrainingDay ?: ""}"
    }
    val statusColor = when (muscle.status) {
        MuscleStatusUi.READY    -> Green
        MuscleStatusUi.PAUSED   -> ForgeColors.Topaz.copy(alpha = 0.5f)
        MuscleStatusUi.UPCOMING -> ForgeColors.Topaz
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = sp.itemSpacing),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = muscle.name,
            fontSize = sp.fontSize.bodyLarge.sp,
            color = ForgeColors.Topaz,
        )
        Text(
            text = statusText,
            fontSize = sp.fontSize.bodyMedium.sp,
            color = statusColor,
        )
    }
}

// Мышечный баланс с прогресс-барами
@Composable
private fun MuscleBalanceSection(muscles: List<MuscleProgressUi>) {
    val sp = LocalForgeSpacing.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = sp.screenPaddingHorizontal),
        verticalArrangement = Arrangement.spacedBy(sp.itemSpacing),
    ) {
        muscles.forEach { muscle ->
            MuscleBalanceRow(muscle = muscle)
        }
    }

    Spacer(modifier = Modifier.height(sp.itemSpacing))
}

@Composable
private fun MuscleBalanceRow(muscle: MuscleProgressUi) {
    val sp = LocalForgeSpacing.current

    val fraction = (muscle.progressPercent / 100f).coerceIn(0f, 1f)

    val barColor = when {
        muscle.status == MuscleStatusUi.PAUSED -> ForgeColors.LazurDark
        fraction >= 0.75f                      -> Green
        else                                   -> ForgeColors.Dawn
    }

    val percentText = if (muscle.status == MuscleStatusUi.PAUSED) "—"
    else "${muscle.progressPercent}%"

    val percentColor = if (muscle.status == MuscleStatusUi.PAUSED) {
        ForgeColors.Topaz.copy(alpha = 0.4f)
    } else {
        ForgeColors.Topaz
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = muscle.name,
            fontSize = sp.fontSize.bodyMedium.sp,
            color = ForgeColors.Topaz,
            modifier = Modifier.width(72.dp),
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(3.dp)
                .background(ForgeColors.LazurDark),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .height(3.dp)
                    .background(barColor),
            )
        }

        Spacer(modifier = Modifier.width(sp.itemSpacing))

        Text(
            text = percentText,
            fontSize = sp.fontSize.labelSmall.sp,
            color = percentColor,
            modifier = Modifier.width(32.dp),
            textAlign = TextAlign.End,
        )
    }
}