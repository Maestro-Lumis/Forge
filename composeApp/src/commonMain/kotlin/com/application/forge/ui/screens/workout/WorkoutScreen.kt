package com.application.forge.ui.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.application.forge.ui.adaptive.LocalForgeSpacing
import com.application.forge.ui.theme.ForgeColors
import kotlin.math.roundToInt
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WorkoutScreen(
    onFinished: () -> Unit,
    viewModel: WorkoutViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.isWorkoutFinished) {
        onFinished()
        return
    }

    if (state.isLoading) {
        WorkoutLoadingContent()
        return
    }

    state.error?.let { error ->
        WorkoutErrorContent(message = error)
        return
    }

    WorkoutContent(
        state = state,
        onCompleteSet = { reps, weight -> viewModel.completeSet(reps, weight) },
        onNextExercise = { viewModel.nextExercise() },
        onRestPreset = { viewModel.setRestPreset(it) },
        onStopTimer = { viewModel.stopRestTimer() },
        onFinish = { viewModel.finishWorkout() },
    )
}

@Composable
private fun WorkoutContent(
    state: WorkoutState,
    onCompleteSet: (Int?, Float?) -> Unit,
    onNextExercise: () -> Unit,
    onRestPreset: (Int) -> Unit,
    onStopTimer: () -> Unit,
    onFinish: () -> Unit,
) {
    val sp = LocalForgeSpacing.current
    val exercise = state.currentExercise ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ForgeColors.Lazur)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = sp.screenPaddingHorizontal),
    ) {
        Spacer(modifier = Modifier.height(sp.sectionSpacing))

        WorkoutHeader(
            title = state.workoutTitle,
            elapsedSeconds = state.elapsedSeconds,
            progressFraction = state.progressFraction,
            streakDays = state.streakDays,
            totalVolumeKg = state.totalVolumeKg,
            onFinish = onFinish,
        )

        Spacer(modifier = Modifier.height(sp.sectionSpacing))

        // Предупреждение о травме
        if (exercise.injuryWarning != null) {
            InjuryWarningBanner(note = exercise.injuryWarning)
            Spacer(modifier = Modifier.height(sp.itemSpacing))
        }

        // Название упражнения и группа мышц
        ExerciseHeader(
            name = exercise.name,
            muscleGroup = exercise.muscleGroup,
            technique = exercise.technique,
        )

        Spacer(modifier = Modifier.height(sp.itemSpacing))

        // Список подходов
        SetsTable(
            sets = exercise.sets,
            currentSetIndex = state.currentSetIndex,
        )

        Spacer(modifier = Modifier.height(sp.sectionSpacing))

        // Таймер отдыха
        RestTimerSection(
            timerState = state.restTimer,
            onPreset = onRestPreset,
            onStop = onStopTimer,
        )

        Spacer(modifier = Modifier.height(sp.sectionSpacing))

        val activeSet = exercise.sets.getOrNull(state.currentSetIndex)
        val allSetsDone = exercise.sets.all { it.status == SetStatus.DONE }

        if (!allSetsDone && activeSet != null) {
            CompleteSetButton(
                set = activeSet,
                onComplete = onCompleteSet,
            )
        }

        if (allSetsDone) {
            Spacer(modifier = Modifier.height(sp.itemSpacing))
            NextExerciseSection(
                nextExercise = state.nextExercise,
                isLastExercise = state.isLastExercise,
                onNext = onNextExercise,
                onFinish = onFinish,
            )
        }

        Spacer(modifier = Modifier.height(sp.sectionSpacing))
    }
}

// Шапка с метриками и прогрессом
@Composable
private fun WorkoutHeader(
    title: String,
    elapsedSeconds: Int,
    progressFraction: Float,
    streakDays: Int,
    totalVolumeKg: Float,
    onFinish: () -> Unit,
) {
    val minutes = elapsedSeconds / 60
    val seconds = elapsedSeconds % 60
    val timeLabel = "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title.uppercase(),
                color = ForgeColors.Icicle,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
            )
            Text(
                text = "СТОП",
                color = ForgeColors.Dawn,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.clickable { onFinish() },
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { progressFraction },
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp),
            color = ForgeColors.Dawn,
            trackColor = ForgeColors.LazurDark,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            MetricChip(label = "ВРЕМЯ", value = timeLabel)
            MetricChip(label = "ОБЪЁМ", value = "${totalVolumeKg.roundToInt()} кг")
            MetricChip(label = "СЕРИЯ", value = "${streakDays}д")
        }
    }
}

@Composable
private fun MetricChip(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            color = ForgeColors.Topaz,
            fontSize = 10.sp,
            letterSpacing = 1.sp,
        )
        Text(
            text = value,
            color = ForgeColors.Icicle,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

// Предупреждение о травме
@Composable
private fun InjuryWarningBanner(note: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ForgeColors.Surface)
            .border(width = 1.dp, color = ForgeColors.Dawn)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Треугольник-предупреждение через текст (без зависимости от material-icons-extended)
        Text(
            text = "⚠",
            color = ForgeColors.Dawn,
            fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = note,
            color = ForgeColors.Dawn,
            fontSize = 12.sp,
        )
    }
}

// Заголовок упражнения
@Composable
private fun ExerciseHeader(
    name: String,
    muscleGroup: String,
    technique: String,
) {
    Column {
        Text(
            text = name.uppercase(),
            color = ForgeColors.Icicle,
            fontSize = 26.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            lineHeight = 28.sp,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = muscleGroup.uppercase(),
            color = ForgeColors.Topaz,
            fontSize = 11.sp,
            letterSpacing = 1.sp,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = technique,
            color = ForgeColors.Topaz,
            fontSize = 12.sp,
            lineHeight = 16.sp,
        )
    }
}

// Таблица подходов
@Composable
private fun SetsTable(
    sets: List<ExerciseSet>,
    currentSetIndex: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ForgeColors.LazurDark)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "ПОДХОД",
                color = ForgeColors.Topaz,
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "ПОВТ",
                color = ForgeColors.Topaz,
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                modifier = Modifier.width(60.dp),
                textAlign = TextAlign.Center,
            )
            Text(
                text = "КГ",
                color = ForgeColors.Topaz,
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                modifier = Modifier.width(60.dp),
                textAlign = TextAlign.Center,
            )
            Text(
                text = "СТАТУС",
                color = ForgeColors.Topaz,
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                modifier = Modifier.width(60.dp),
                textAlign = TextAlign.End,
            )
        }

        sets.forEachIndexed { index, set ->
            SetRow(
                set = set,
                isActive = index == currentSetIndex,
            )
        }
    }
}

@Composable
private fun SetRow(set: ExerciseSet, isActive: Boolean) {
    val textColor = when {
        set.status == SetStatus.DONE -> ForgeColors.Topaz
        isActive -> ForgeColors.Icicle
        else -> ForgeColors.LazurDeep
    }
    val bgColor = if (isActive) ForgeColors.Lazur else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "${set.index + 1}",
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
        )
        val repsDisplay = set.actualReps?.toString() ?: set.targetReps.toString()
        Text(
            text = repsDisplay,
            color = textColor,
            fontSize = 14.sp,
            modifier = Modifier.width(60.dp),
            textAlign = TextAlign.Center,
        )
        val weightDisplay = set.actualWeight?.toString()
            ?: set.targetWeight?.toString()
            ?: "-"
        Text(
            text = weightDisplay,
            color = textColor,
            fontSize = 14.sp,
            modifier = Modifier.width(60.dp),
            textAlign = TextAlign.Center,
        )
        val statusLabel = when (set.status) {
            SetStatus.DONE -> "✓"
            SetStatus.ACTIVE -> "●"
            SetStatus.PENDING -> "○"
        }
        val statusColor = when (set.status) {
            SetStatus.DONE -> ForgeColors.Topaz
            SetStatus.ACTIVE -> ForgeColors.Dawn
            SetStatus.PENDING -> ForgeColors.LazurDark
        }
        Text(
            text = statusLabel,
            color = statusColor,
            fontSize = 16.sp,
            modifier = Modifier.width(60.dp),
            textAlign = TextAlign.End,
        )
    }
}

// Таймер отдыха
@Composable
private fun RestTimerSection(
    timerState: RestTimerState,
    onPreset: (Int) -> Unit,
    onStop: () -> Unit,
) {
    val presets = listOf(60, 90, 120, 180)

    Column {
        Text(
            text = "ОТДЫХ",
            color = ForgeColors.Topaz,
            fontSize = 10.sp,
            letterSpacing = 2.sp,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val timerColor = if (timerState.isRunning) ForgeColors.Dawn else ForgeColors.Topaz
            val min = timerState.remainingSeconds / 60
            val sec = timerState.remainingSeconds % 60
            val secStr = sec.toString().padStart(2, '0')
            Text(
                text = "$min:$secStr",
                color = timerColor,
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.End,
            ) {
                presets.forEach { preset ->
                    val isSelected = timerState.totalSeconds == preset
                    val labelColor = if (isSelected) ForgeColors.Dawn else ForgeColors.Topaz
                    Text(
                        text = "${preset}с",
                        color = labelColor,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.clickable { onPreset(preset) },
                    )
                }
            }
        }

        if (timerState.isRunning) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "СТОП ТАЙМЕР",
                color = ForgeColors.Topaz,
                fontSize = 11.sp,
                letterSpacing = 1.sp,
                modifier = Modifier.clickable { onStop() },
            )
        }
    }
}

// Кнопка завершения подхода
@Composable
private fun CompleteSetButton(
    set: ExerciseSet,
    onComplete: (Int?, Float?) -> Unit,
) {
    var repsInput by remember(set.index) {
        mutableStateOf(set.targetReps.toString())
    }
    var weightInput by remember(set.index) {
        mutableStateOf(set.targetWeight?.toString() ?: "")
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ForgeInputField(
                label = "ПОВТ",
                value = repsInput,
                onValueChange = { repsInput = it },
                modifier = Modifier.weight(1f),
            )
            ForgeInputField(
                label = "ВЕС, КГ",
                value = weightInput,
                onValueChange = { weightInput = it },
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val reps = repsInput.toIntOrNull()
                val weight = weightInput.toFloatOrNull()
                onComplete(reps, weight)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = ForgeColors.Dawn,
                contentColor = ForgeColors.OnCTA,
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
        ) {
            Text(
                text = "ПОДХОД ЗАВЕРШЁН",
                fontWeight = FontWeight.Black,
                fontSize = 14.sp,
                letterSpacing = 2.sp,
            )
        }
    }
}

@Composable
private fun ForgeInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = ForgeColors.Topaz,
            fontSize = 10.sp,
            letterSpacing = 1.sp,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ForgeColors.LazurDark)
                .border(width = 1.dp, color = ForgeColors.Topaz)
                .padding(horizontal = 12.dp, vertical = 10.dp),
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    color = ForgeColors.Icicle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
            )
        }
    }
}

// Следующее упражнение / завершение
@Composable
private fun NextExerciseSection(
    nextExercise: ExerciseUiModel?,
    isLastExercise: Boolean,
    onNext: () -> Unit,
    onFinish: () -> Unit,
) {
    Column {
        if (!isLastExercise && nextExercise != null) {
            Text(
                text = "СЛЕДУЮЩЕЕ",
                color = ForgeColors.Topaz,
                fontSize = 10.sp,
                letterSpacing = 2.sp,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = nextExercise.name.uppercase(),
                color = ForgeColors.Icicle,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = nextExercise.muscleGroup.uppercase(),
                color = ForgeColors.Topaz,
                fontSize = 11.sp,
                letterSpacing = 1.sp,
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        val buttonLabel = if (isLastExercise) "ЗАВЕРШИТЬ ТРЕНИРОВКУ" else "СЛЕДУЮЩЕЕ УПРАЖНЕНИЕ"
        val buttonAction = if (isLastExercise) onFinish else onNext

        Button(
            onClick = buttonAction,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isLastExercise) ForgeColors.Dawn else ForgeColors.LazurDark,
                contentColor = if (isLastExercise) ForgeColors.OnCTA else ForgeColors.Icicle,
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            border = if (!isLastExercise) {
                androidx.compose.foundation.BorderStroke(1.dp, ForgeColors.Topaz)
            } else null,
        ) {
            Text(
                text = buttonLabel,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 2.sp,
            )
        }
    }
}

// Загрузка
@Composable
private fun WorkoutLoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ForgeColors.Lazur),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = ForgeColors.Dawn)
    }
}

// Ошибка
@Composable
private fun WorkoutErrorContent(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ForgeColors.Lazur)
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            color = ForgeColors.Dawn,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
        )
    }
}