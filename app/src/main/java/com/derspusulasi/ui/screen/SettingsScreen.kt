package com.derspusulasi.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import com.derspusulasi.data.model.Settings
import com.derspusulasi.data.repository.SettingsRepository
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    settingsRepository: SettingsRepository,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: SettingsViewModel = viewModel()
    var settings by remember { mutableStateOf<Settings?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var brightnessValue by remember { mutableFloatStateOf(-1f) }
    var useSystemBrightness by remember { mutableStateOf(true) }
    var showSuccessMessage by remember { mutableStateOf(false) }
    
    // Load settings
    LaunchedEffect(Unit) {
        try {
            val loadedSettings = settingsRepository.getSettings()
            settings = loadedSettings
            brightnessValue = loadedSettings.screenBrightness
            useSystemBrightness = loadedSettings.screenBrightness < 0
            isLoading = false
        } catch (e: Exception) {
            isLoading = false
        }
    }
    
    // Function to update brightness in MainActivity
    val updateBrightness: (Float) -> Unit = { brightness ->
        try {
            val activity = context as? com.derspusulasi.MainActivity
            activity?.updateScreenBrightness(brightness)
        } catch (e: Exception) {
            // Handle error silently
        }
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                    contentDescription = "Geri"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Ayarlar",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Ekran Parlaklığı",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    
                    // System brightness toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Sistem ayarını kullan",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black
                        )
                        Switch(
                            checked = useSystemBrightness,
                            onCheckedChange = { checked ->
                                useSystemBrightness = checked
                                if (checked) {
                                    brightnessValue = -1f
                                } else {
                                    // Set to current system brightness or default
                                    brightnessValue = 0.5f
                                }
                            }
                        )
                    }
                    
                    if (!useSystemBrightness) {
                        Column {
                            Text(
                                text = "Parlaklık: ${(brightnessValue * 100).toInt()}%",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Slider(
                                value = brightnessValue,
                                onValueChange = { newValue ->
                                    brightnessValue = newValue
                                },
                                valueRange = 0f..1f,
                                steps = 19 // 0%, 5%, 10%, ..., 100%
                            )
                        }
                    }
                    
                    // Save button
                    Button(
                        onClick = {
                            val currentSettings = settings ?: Settings()
                            val updatedSettings = currentSettings.copy(
                                screenBrightness = brightnessValue
                            )
                            // Update brightness immediately
                            updateBrightness(brightnessValue)
                            
                            // Save settings asynchronously
                            viewModel.saveSettings(
                                settingsRepository,
                                updatedSettings
                            )
                            
                            showSuccessMessage = true
                            settings = updatedSettings
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Kaydet")
                    }
                    
                    if (showSuccessMessage) {
                        LaunchedEffect(showSuccessMessage) {
                            kotlinx.coroutines.delay(2000)
                            showSuccessMessage = false
                        }
                        Text(
                            text = "Ayarlar kaydedildi!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            // Real-time brightness update when slider changes
            LaunchedEffect(brightnessValue) {
                if (!useSystemBrightness) {
                    updateBrightness(brightnessValue)
                }
            }
            
            // Update brightness when toggle changes
            LaunchedEffect(useSystemBrightness) {
                updateBrightness(if (useSystemBrightness) -1f else brightnessValue)
            }
        }
    }
}

// Helper ViewModel for settings operations
class SettingsViewModel : androidx.lifecycle.ViewModel() {
    fun saveSettings(
        settingsRepository: SettingsRepository,
        settings: Settings
    ) {
        viewModelScope.launch {
            try {
                settingsRepository.saveSettings(settings)
            } catch (e: Exception) {
                // Handle error silently
            }
        }
    }
}
