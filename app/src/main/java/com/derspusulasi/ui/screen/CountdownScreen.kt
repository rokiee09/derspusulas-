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
import com.derspusulasi.ui.viewmodel.CountdownViewModel
import com.derspusulasi.util.NotificationHelper
import java.util.concurrent.TimeUnit

@Composable
fun CountdownScreen(
    viewModel: CountdownViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var minutesInput by remember { mutableStateOf("0") }
    var secondsInput by remember { mutableStateOf("0") }
    val context = androidx.compose.ui.platform.LocalContext.current
    val haptic = LocalHapticFeedback.current

    var previousRemainingSeconds by remember { mutableStateOf(uiState.remainingSeconds) }
    
    LaunchedEffect(uiState.remainingSeconds, uiState.isRunning) {
        // Check if timer just finished
        if (previousRemainingSeconds > 0 && 
            uiState.remainingSeconds == 0L && 
            !uiState.isRunning && 
            !uiState.isPaused &&
            previousRemainingSeconds != uiState.remainingSeconds) {
            NotificationHelper.showCountdownFinishedNotification(context)
        }
        previousRemainingSeconds = uiState.remainingSeconds
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
            // Geri Sayım icon
            Image(
                painter = painterResource(id = R.drawable.geri_saym),
                contentDescription = "Geri Sayım",
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

            val primaryColor = MaterialTheme.colorScheme.primary

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
                    color = primaryColor,
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
                val minutes = TimeUnit.SECONDS.toMinutes(uiState.remainingSeconds)
                val seconds = uiState.remainingSeconds % 60
                Text(
                    text = String.format("%02d:%02d", minutes, seconds),
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Time Input
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Süre Ayarla",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Dakika")
                        TextField(
                            value = minutesInput,
                            onValueChange = { newValue ->
                                // Allow only digits
                                if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                                    minutesInput = newValue
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isRunning,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Saniye")
                        TextField(
                            value = secondsInput,
                            onValueChange = { newValue ->
                                // Allow only digits
                                if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                                    secondsInput = newValue
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isRunning,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                    }
                }

                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        val minutes = minutesInput.toIntOrNull() ?: 0
                        val seconds = secondsInput.toIntOrNull() ?: 0
                        viewModel.setTime(minutes, seconds)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isRunning
                ) {
                    Text("Ayarla")
                }
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
                    modifier = Modifier.weight(1f),
                    enabled = uiState.remainingSeconds > 0
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
    }
}
