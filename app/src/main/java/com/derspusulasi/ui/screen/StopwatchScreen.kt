package com.derspusulasi.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import com.derspusulasi.R
import com.derspusulasi.ui.components.MotivationCard
import com.derspusulasi.ui.viewmodel.StopwatchViewModel
import java.util.concurrent.TimeUnit

@Composable
fun StopwatchScreen(
    viewModel: StopwatchViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val haptic = LocalHapticFeedback.current

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
            // Kronometre icon
            Image(
                painter = painterResource(id = R.drawable.kronometre),
                contentDescription = "Kronometre",
                modifier = Modifier.size(64.dp)
            )
        }

        // Timer Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            val hours = TimeUnit.MILLISECONDS.toHours(uiState.elapsedTime).toInt()
            val minutes = (TimeUnit.MILLISECONDS.toMinutes(uiState.elapsedTime) % 60).toInt()
            val seconds = (TimeUnit.MILLISECONDS.toSeconds(uiState.elapsedTime) % 60).toInt()
            val milliseconds = ((uiState.elapsedTime % 1000) / 10).toInt()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, milliseconds),
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
                        viewModel.pauseStopwatch()
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

                OutlinedButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.addLap()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Flag, contentDescription = "Tur")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tur")
                }
            } else {
                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.startStopwatch()
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
                    viewModel.resetStopwatch()
                },
                modifier = Modifier.weight(1f),
                enabled = uiState.elapsedTime > 0 || uiState.laps.isNotEmpty()
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Sıfırla")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sıfırla")
            }
        }

        // Laps
        if (uiState.laps.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Turlar (${uiState.laps.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.height(200.dp)
                    ) {
                        items(uiState.laps.reversed()) { lap ->
                            LapItem(lap = lap)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LapItem(
    lap: com.derspusulasi.ui.viewmodel.Lap,
    modifier: Modifier = Modifier
) {
    val hours = TimeUnit.MILLISECONDS.toHours(lap.time).toInt()
    val minutes = (TimeUnit.MILLISECONDS.toMinutes(lap.time) % 60).toInt()
    val seconds = (TimeUnit.MILLISECONDS.toSeconds(lap.time) % 60).toInt()
    val milliseconds = ((lap.time % 1000) / 10).toInt()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tur ${lap.lapNumber}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, milliseconds),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
