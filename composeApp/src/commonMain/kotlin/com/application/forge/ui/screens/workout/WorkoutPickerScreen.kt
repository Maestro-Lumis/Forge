package com.application.forge.ui.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.forge.domain.model.Workout
import com.application.forge.domain.usecase.GetAllWorkoutsUseCase
import com.application.forge.ui.adaptive.LocalForgeSpacing
import com.application.forge.ui.screens.dashboard.ForgeDivider
import com.application.forge.ui.theme.ForgeColors
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun WorkoutPickerScreen(
    onWorkoutSelected: (workoutId: String) -> Unit,
    onBack: () -> Unit,
    getAllWorkouts: GetAllWorkoutsUseCase = koinInject(),
) {
    val sp = LocalForgeSpacing.current
    val scope = rememberCoroutineScope()

    var workouts by remember { mutableStateOf<List<Workout>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        workouts = getAllWorkouts()
        isLoading = false
    }

    if (isLoading) {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ForgeColors.Lazur)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .verticalScroll(rememberScrollState()),
    ) {
        // Шапка
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
                text = "←",
                fontSize = 20.sp,
                color = ForgeColors.Topaz,
                modifier = Modifier.clickable { onBack() },
            )
            Text(
                text = "ВЫБОР ТРЕНИРОВКИ",
                fontSize = sp.fontSize.labelSmall.sp,
                letterSpacing = 2.sp,
                color = ForgeColors.Topaz,
            )
            // Пустой элемент для центрирования заголовка
            Text(
                text = "←",
                fontSize = 20.sp,
                color = ForgeColors.Lazur,
            )
        }

        ForgeDivider()

        Spacer(modifier = Modifier.height(sp.sectionSpacing))

        Text(
            text = "ПЛАН",
            fontSize = sp.fontSize.headlineLarge.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            color = ForgeColors.Icicle,
            modifier = Modifier.padding(horizontal = sp.screenPaddingHorizontal),
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Выбери тренировку для старта",
            fontSize = sp.fontSize.bodyMedium.sp,
            color = ForgeColors.Topaz,
            modifier = Modifier.padding(horizontal = sp.screenPaddingHorizontal),
        )

        Spacer(modifier = Modifier.height(sp.sectionSpacing))

        // Карточки тренировок
        workouts.forEach { workout ->
            WorkoutPickerCard(
                workout = workout,
                onStart = { onWorkoutSelected(workout.id) },
            )
            Spacer(modifier = Modifier.height(sp.itemSpacing))
        }

        Spacer(modifier = Modifier.height(sp.sectionSpacing))
    }
}

@Composable
private fun WorkoutPickerCard(
    workout: Workout,
    onStart: () -> Unit,
) {
    val sp = LocalForgeSpacing.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = sp.screenPaddingHorizontal)
            .background(ForgeColors.LazurDark)
            .padding(sp.cardPadding),
    ) {
        // День и группы мышц
        Text(
            text = workout.dayName,
            fontSize = sp.fontSize.labelSmall.sp,
            letterSpacing = 2.sp,
            color = ForgeColors.Topaz,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = workout.muscleGroups,
            fontSize = sp.fontSize.headlineMedium.sp,
            fontWeight = FontWeight.Bold,
            color = ForgeColors.Icicle,
        )

        Spacer(modifier = Modifier.height(sp.itemSpacing))

        // Метрики
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(sp.itemSpacing),
        ) {
            PickerChip(
                label = "УПРАЖНЕНИЙ",
                value = "${workout.exercises.size}",
            )
            PickerChip(
                label = "МИНУТ",
                value = "~${workout.estimatedDurationMin}",
            )
        }

        Spacer(modifier = Modifier.height(sp.itemSpacing))

        // Список упражнений кратко
        workout.exercises.forEach { exercise ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(12.dp)
                        .background(ForgeColors.Topaz.copy(alpha = 0.4f)),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${exercise.name}  ${exercise.sets}×${exercise.reps}",
                    fontSize = sp.fontSize.labelSmall.sp,
                    color = ForgeColors.Topaz,
                )
            }
        }

        Spacer(modifier = Modifier.height(sp.itemSpacing))

        // Кнопка старта
        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = ForgeColors.Dawn,
                contentColor = ForgeColors.OnCTA,
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
        ) {
            Text(
                text = "НАЧАТЬ",
                fontWeight = FontWeight.Black,
                fontSize = sp.fontSize.bodyMedium.sp,
                letterSpacing = 2.sp,
            )
        }
    }
}

@Composable
private fun PickerChip(label: String, value: String) {
    val sp = LocalForgeSpacing.current

    Column(
        modifier = Modifier
            .background(ForgeColors.LazurDeep)
            .padding(horizontal = sp.cardPadding, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = value,
            fontSize = sp.fontSize.bodyLarge.sp,
            fontWeight = FontWeight.Bold,
            color = ForgeColors.Icicle,
        )
        Text(
            text = label,
            fontSize = sp.fontSize.labelSmall.sp,
            color = ForgeColors.Topaz,
            letterSpacing = 1.sp,
        )
    }
}