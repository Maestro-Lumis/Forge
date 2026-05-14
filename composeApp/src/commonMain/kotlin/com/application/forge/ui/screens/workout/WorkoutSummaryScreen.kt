package com.application.forge.ui.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.forge.ui.adaptive.LocalForgeSpacing
import com.application.forge.ui.theme.ForgeColors
import kotlin.math.roundToInt

@Composable
fun WorkoutSummaryScreen(
    state: WorkoutState,
    onBack: () -> Unit,
) {
    val sp = LocalForgeSpacing.current

    val totalSets = state.exercises.sumOf { ex ->
        ex.sets.count { it.status == SetStatus.DONE }
    }
    val totalReps = state.exercises.sumOf { ex ->
        ex.sets
            .filter { it.status == SetStatus.DONE }
            .sumOf { it.actualReps ?: 0 }
    }

    val minutes = state.elapsedSeconds / 60
    val seconds = state.elapsedSeconds % 60
    val timeLabel = "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ForgeColors.Lazur)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = sp.screenPaddingHorizontal),
    ) {
        Spacer(modifier = Modifier.height(sp.sectionSpacing))

        Text(
            text = "ТРЕНИРОВКА",
            color = ForgeColors.Topaz,
            fontSize = 11.sp,
            letterSpacing = 2.sp,
        )
        Text(
            text = "ЗАВЕРШЕНА",
            color = ForgeColors.Dawn,
            fontSize = 36.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
            lineHeight = 38.sp,
        )

        Spacer(modifier = Modifier.height(sp.sectionSpacing))

        // Метрики — строка 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            SummaryMetric(label = "ВРЕМЯ", value = timeLabel)
            SummaryMetric(label = "ОБЪЁМ", value = "${state.totalVolumeKg.roundToInt()} кг")
            SummaryMetric(label = "СЕРИЯ", value = "${state.streakDays}д")
        }

        Spacer(modifier = Modifier.height(sp.itemSpacing))

        // Метрики — строка 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            SummaryMetric(label = "ПОДХОДОВ", value = "$totalSets")
            SummaryMetric(label = "ПОВТОРЕНИЙ", value = "$totalReps")
            SummaryMetric(label = "УПРАЖНЕНИЙ", value = "${state.exercises.size}")
        }

        Spacer(modifier = Modifier.height(sp.sectionSpacing))

        // Разделитель
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(ForgeColors.LazurDark),
        )

        Spacer(modifier = Modifier.height(sp.sectionSpacing))

        Text(
            text = "ДЕТАЛИ",
            color = ForgeColors.Topaz,
            fontSize = 10.sp,
            letterSpacing = 2.sp,
        )

        Spacer(modifier = Modifier.height(sp.itemSpacing))

        state.exercises.forEach { exercise ->
            ExerciseSummaryRow(exercise = exercise)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(sp.sectionSpacing))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = ForgeColors.Dawn,
                contentColor = ForgeColors.OnCTA,
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
        ) {
            Text(
                text = "НА ГЛАВНУЮ",
                fontWeight = FontWeight.Black,
                fontSize = 14.sp,
                letterSpacing = 2.sp,
            )
        }

        Spacer(modifier = Modifier.height(sp.sectionSpacing))
    }
}

@Composable
private fun SummaryMetric(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = ForgeColors.Icicle,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
        )
        Text(
            text = label,
            color = ForgeColors.Topaz,
            fontSize = 10.sp,
            letterSpacing = 1.sp,
        )
    }
}

@Composable
private fun ExerciseSummaryRow(exercise: ExerciseUiModel) {
    val doneSets = exercise.sets.filter { it.status == SetStatus.DONE }
    val totalDone = doneSets.size
    val totalSets = exercise.sets.size

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ForgeColors.LazurDark)
            .padding(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = exercise.name.uppercase(),
                color = ForgeColors.Icicle,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "$totalDone/$totalSets подх.",
                color = if (totalDone == totalSets) ForgeColors.Dawn else ForgeColors.Topaz,
                fontSize = 12.sp,
            )
        }

        if (doneSets.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            val setLabels = doneSets.joinToString("  ") { set ->
                val reps = set.actualReps ?: "-"
                val weight = set.actualWeight?.let { "${it}кг" } ?: ""
                "$reps×$weight"
            }
            Text(
                text = setLabels,
                color = ForgeColors.Topaz,
                fontSize = 12.sp,
            )
        }
    }
}