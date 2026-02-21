package com.derspusulasi.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.derspusulasi.R
import com.derspusulasi.ui.components.MotivationCard
import com.derspusulasi.ui.viewmodel.PomodoroViewModel
import com.derspusulasi.util.NotificationHelper
import java.util.concurrent.TimeUnit

@Composable
fun PomodoroScreen(
    viewModel: PomodoroViewModel,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Local state for TextField inputs to allow manual editing
    var focusMinutesInput by remember { mutableStateOf(uiState.focusMinutes.toString()) }
    var breakMinutesInput by remember { mutableStateOf(uiState.breakMinutes.toString()) }
    
    // Update local state when ViewModel state changes
    LaunchedEffect(uiState.focusMinutes) {
        focusMinutesInput = uiState.focusMinutes.toString()
    }
    LaunchedEffect(uiState.breakMinutes) {
        breakMinutesInput = uiState.breakMinutes.toString()
    }

    var previousRemainingSeconds by remember { mutableStateOf(uiState.remainingSeconds) }
    var previousIsRunning by remember { mutableStateOf(uiState.isRunning) }
    var previousIsFocusMode by remember { mutableStateOf(uiState.isFocusMode) }
    
    LaunchedEffect(uiState.remainingSeconds, uiState.isRunning, uiState.isFocusMode) {
        // Check if timer just finished (was running, now stopped, and reached 0)
        if (previousIsRunning && 
            !uiState.isRunning && 
            !uiState.isPaused &&
            uiState.remainingSeconds == 0L &&
            previousRemainingSeconds > 0) {
            // Use the previous mode (before it switches)
            NotificationHelper.showPomodoroFinishedNotification(
                context,
                previousIsFocusMode // Show notification for the mode that just finished
            )
        }
        previousRemainingSeconds = uiState.remainingSeconds
        previousIsRunning = uiState.isRunning
        previousIsFocusMode = uiState.isFocusMode
    }

    // Background is handled in MainActivity
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Back button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onBackClick()
                }
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
            }
            Spacer(modifier = Modifier.width(8.dp))
            // Pomodoro icon
            Image(
                painter = painterResource(id = R.drawable.pomodoro),
                contentDescription = "Pomodoro",
                modifier = Modifier.size(64.dp)
            )
        }

        // Timer Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            val progress = if (uiState.totalSeconds > 0) {
                uiState.remainingSeconds.toFloat() / uiState.totalSeconds.toFloat()
            } else {
                1f
            }
            val animatedProgress by animateFloatAsState(
                targetValue = progress,
                label = "progress"
            )

            val focusColor = MaterialTheme.colorScheme.primary
            val breakColor = MaterialTheme.colorScheme.secondary

            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 20.dp.toPx()
                val radius = (size.minDimension - strokeWidth) / 2

                // Background circle
                drawCircle(
                    color = Color.Gray.copy(alpha = 0.2f),
                    radius = radius,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Progress circle
                val sweepAngle = 360f * animatedProgress
                drawArc(
                    color = if (uiState.isFocusMode) {
                        focusColor
                    } else {
                        breakColor
                    },
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    topLeft = androidx.compose.ui.geometry.Offset(
                        (size.width - radius * 2) / 2,
                        (size.height - radius * 2) / 2
                    ),
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (uiState.isFocusMode) "ODAK" else "MOLA",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                val minutes = TimeUnit.SECONDS.toMinutes(uiState.remainingSeconds)
                val seconds = uiState.remainingSeconds % 60
                Text(
                    text = String.format("%02d:%02d", minutes, seconds),
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (uiState.isRunning) {
                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.pauseTimer()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(Icons.Default.Pause, contentDescription = "Duraklat")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Duraklat")
                }
            } else {
                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.startTimer()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Başlat")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Başlat")
                }
            }

            OutlinedButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.resetTimer()
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Sıfırla")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sıfırla")
            }
        }

        // Settings
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Ayarlar",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Odak Süresi (dk)")
                        TextField(
                            value = focusMinutesInput,
                            onValueChange = { newValue ->
                                // Allow typing any text (including empty)
                                if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                                    focusMinutesInput = newValue
                                    // Update ViewModel when valid number is entered
                                    newValue.toIntOrNull()?.let { minutes ->
                                        if (minutes > 0) {
                                            viewModel.updateFocusMinutes(minutes)
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isRunning,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Mola Süresi (dk)")
                        TextField(
                            value = breakMinutesInput,
                            onValueChange = { newValue ->
                                // Allow typing any text (including empty)
                                if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                                    breakMinutesInput = newValue
                                    // Update ViewModel when valid number is entered
                                    newValue.toIntOrNull()?.let { minutes ->
                                        if (minutes > 0) {
                                            viewModel.updateBreakMinutes(minutes)
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isRunning,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }
            }
        }
    }
}
