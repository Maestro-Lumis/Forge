package com.application.forge.ui.screens.dashboard

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.forge.ui.adaptive.LocalForgeSpacing
import com.application.forge.ui.theme.ForgeColors

// Dashboard Screen
@Composable
fun DashboardScreen(
    state: DashboardState,
    onEvent: (DashboardEvent) -> Unit,
) {
    val sp = LocalForgeSpacing.current

    // Загрузка
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ForgeColors.Lazur),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                color = ForgeColors.Dawn,
            )
        }
        return
    }

    // Ошибка
    state.error?.let { errorMsg ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ForgeColors.Lazur),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = errorMsg,
                    color = ForgeColors.Topaz,
                    fontSize = sp.fontSize.bodyLarge.sp,
                )
                Spacer(modifier = Modifier.height(sp.itemSpacing))
                Box(
                    modifier = Modifier
                        .background(ForgeColors.Dawn)
                        .clickable { onEvent(DashboardEvent.DismissError) }
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

    // Основной контент
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ForgeColors.Lazur)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .verticalScroll(rememberScrollState()),
    ) {
        ForgeTopBar(streak = state.streakDays)

        state.todayWorkout?.let { workout ->
            HeroWorkoutCard(
                workout = workout,
                injuries = state.injuries,
                onStart = { onEvent(DashboardEvent.StartWorkout) },
            )
        }

        ForgeDivider()

        StatsRow(
            weightKg = state.weightKg,
            goalKg = state.goalKg,
            startKg = state.startKg,
        )

        ForgeDivider()

        SectionLabel(text = "ВОССТАНОВЛЕНИЕ")
        RecoverySection(muscles = state.muscleStatuses)

        ForgeDivider()

        SectionLabel(text = "МЫШЕЧНЫЙ БАЛАНС")
        MuscleBalanceSection(muscles = state.muscleStatuses)

        Spacer(modifier = Modifier.height(sp.sectionSpacing))
    }
}

// Top Bar

@Composable
fun ForgeTopBar(streak: Int) {
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
            text = "$streak дн подряд",
            fontSize = sp.fontSize.labelSmall.sp,
            letterSpacing = 1.sp,
            color = ForgeColors.Dawn,
        )

        Box(
            modifier = Modifier
                .size(sp.topBarHeight * 0.55f)
                .background(
                    color = ForgeColors.LazurDark,
                    shape = MaterialTheme.shapes.large,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "U",
                fontSize = sp.fontSize.bodyMedium.sp,
                color = ForgeColors.Topaz,
            )
        }
    }
}

// Hero Card — сегодняшняя тренировка

@Composable
fun HeroWorkoutCard(
    workout: TodayWorkoutUi,
    injuries: List<InjuryWarningUi>,
    onStart: () -> Unit,
) {
    val sp = LocalForgeSpacing.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = sp.screenPaddingHorizontal),
    ) {
        // День недели
        Text(
            text = workout.dayName,
            fontSize = sp.fontSize.labelSmall.sp,
            letterSpacing = 2.sp,
            color = ForgeColors.Topaz,
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Название тренировки
        Text(
            text = workout.muscleGroups,
            fontSize = sp.fontSize.headlineLarge.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = (-0.5).sp,
            color = ForgeColors.Icicle,
        )

        Spacer(modifier = Modifier.height(sp.itemSpacing))

        // Инфо строка
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ForgeColors.LazurDark)
                .padding(sp.cardPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = "${workout.exerciseCount} упражнений",
                    fontSize = sp.fontSize.bodyLarge.sp,
                    color = ForgeColors.Icicle,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "~${workout.durationMin} мин",
                    fontSize = sp.fontSize.bodyMedium.sp,
                    color = ForgeColors.Topaz,
                )
            }
            Text(
                text = "›",
                fontSize = sp.fontSize.headlineMedium.sp,
                color = ForgeColors.Topaz,
            )
        }

        // Предупреждения о травмах
        injuries.forEach { injury ->
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ForgeColors.Surface)
                    .padding(sp.cardPadding),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(20.dp)
                        .background(ForgeColors.Dawn),
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = injury.warning,
                    fontSize = sp.fontSize.labelSmall.sp,
                    letterSpacing = 1.sp,
                    color = ForgeColors.Topaz,
                )
            }
        }

        Spacer(modifier = Modifier.height(sp.itemSpacing))

        // Кнопка начать
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ForgeColors.Dawn)
                .clickable { onStart() }
                .padding(vertical = sp.cardPadding),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "НАЧАТЬ ТРЕНИРОВКУ",
                fontSize = sp.fontSize.bodyMedium.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 2.sp,
                color = ForgeColors.OnCTA,
            )
        }

        Spacer(modifier = Modifier.height(sp.sectionSpacing))
    }
}


// Stats Row
@Composable
fun StatsRow(
    weightKg: Float,
    goalKg: Float,
    startKg: Float,
) {
    val sp = LocalForgeSpacing.current
    val lost = startKg - weightKg
    val progress = ((startKg - weightKg) / (startKg - goalKg)).coerceIn(0f, 1f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = sp.screenPaddingHorizontal,
                vertical = sp.screenPaddingVertical,
            ),
        horizontalArrangement = Arrangement.spacedBy(sp.itemSpacing),
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            value = "${weightKg.toInt()} кг",
            label = "ВЕС СЕЙЧАС",
            sub = "-${lost.toInt()} кг за 7 мес",
            subColor = Color(0xFF1D9E75),
            progress = progress,
            barColor = Color(0xFF1D9E75),
        )
        StatCard(
            modifier = Modifier.weight(1f),
            value = "${goalKg.toInt()}–95 кг",
            label = "ЦЕЛЬ",
            sub = "~${(weightKg - goalKg).toInt()} кг осталось",
            subColor = ForgeColors.Topaz,
            progress = progress,
            barColor = ForgeColors.Dawn,
        )
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    sub: String,
    subColor: Color,
    progress: Float,
    barColor: Color,
) {
    val sp = LocalForgeSpacing.current

    Column(
        modifier = modifier
            .background(ForgeColors.LazurDark)
            .padding(sp.cardPadding),
    ) {
        Text(
            text = label,
            fontSize = sp.fontSize.labelSmall.sp,
            letterSpacing = 1.5.sp,
            color = ForgeColors.Topaz,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = sp.fontSize.headlineMedium.sp,
            fontWeight = FontWeight.Medium,
            color = ForgeColors.Icicle,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = sub,
            fontSize = sp.fontSize.labelSmall.sp,
            color = subColor,
        )
        Spacer(modifier = Modifier.height(sp.itemSpacing))

        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(ForgeColors.LazurDeep),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(2.dp)
                    .background(barColor),
            )
        }
    }
}

// Recovery Sectio
@Composable
fun RecoverySection(muscles: List<MuscleStatusUi>) {
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
fun RecoveryRow(muscle: MuscleStatusUi) {
    val sp = LocalForgeSpacing.current

    val (statusText, statusColor) = when (muscle.status) {
        RecoveryStatusUi.READY    -> "Готова ✓"   to Color(0xFF1D9E75)
        RecoveryStatusUi.PAUSED   -> "На паузе"   to ForgeColors.Topaz.copy(alpha = 0.5f)
        RecoveryStatusUi.UPCOMING -> "Чт · 2 дня" to ForgeColors.Topaz
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

// Muscle Balance Sectio
@Composable
fun MuscleBalanceSection(muscles: List<MuscleStatusUi>) {
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
fun MuscleBalanceRow(muscle: MuscleStatusUi) {
    val sp = LocalForgeSpacing.current

    val barColor = when {
        muscle.status == RecoveryStatusUi.PAUSED -> ForgeColors.LazurDark
        muscle.progress >= 0.75f                -> Color(0xFF1D9E75)
        else                                    -> ForgeColors.Dawn
    }

    val percentText = if (muscle.status == RecoveryStatusUi.PAUSED) {
        "—"
    } else {
        "${(muscle.progress * 100).toInt()}%"
    }

    val percentColor = if (muscle.status == RecoveryStatusUi.PAUSED) {
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

        // Прогресс бар
        Box(
            modifier = Modifier
                .weight(1f)
                .height(3.dp)
                .background(ForgeColors.LazurDark),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(muscle.progress)
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

// Helpers
@Composable
fun SectionLabel(text: String) {
    val sp = LocalForgeSpacing.current

    Text(
        text = text,
        fontSize = sp.fontSize.labelSmall.sp,
        letterSpacing = 2.sp,
        color = ForgeColors.Topaz.copy(alpha = 0.6f),
        modifier = Modifier.padding(
            horizontal = sp.screenPaddingHorizontal,
            vertical = sp.itemSpacing,
        ),
    )
}

@Composable
fun ForgeDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(ForgeColors.LazurDark),
    )
}

@Composable
fun ForgeDividerThin() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .alpha(0.4f)
            .background(ForgeColors.LazurDark),
    )
}