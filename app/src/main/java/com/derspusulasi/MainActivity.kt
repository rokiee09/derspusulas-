package com.derspusulasi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.util.Log
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.derspusulasi.R
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.derspusulasi.data.datasource.FileDataSource
import com.derspusulasi.data.repository.*
import com.derspusulasi.ui.screen.*
import com.derspusulasi.ui.theme.DersPusulasiTheme
import com.derspusulasi.ui.viewmodel.*
import com.derspusulasi.util.NotificationHelper
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var settingsRepository: SettingsRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen early for better performance
        val splashScreen = installSplashScreen()
        
        super.onCreate(savedInstanceState)

        try {
            // Initialize repositories (lightweight operations)
            val fileDataSource = FileDataSource(this)
            settingsRepository = SettingsRepository(fileDataSource)
            val todoRepository = TodoRepository(fileDataSource)
            val questionRepository = QuestionRepository(fileDataSource)

            // Load and apply screen brightness setting asynchronously
            lifecycleScope.launch {
                try {
                    val settings = settingsRepository.getSettings()
                    applyScreenBrightness(settings.screenBrightness)
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error loading settings", e)
                }
            }

            // Create notification channel (non-blocking)
            NotificationHelper.createNotificationChannel(this)

            setContent {
                // Background Image - OUTSIDE MaterialTheme to ensure it's visible
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.background),
                        contentDescription = "Arka plan görseli",
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer { alpha = 0.8f }, // Saydamlık: 0.8 (daha belirgin)
                        contentScale = ContentScale.FillBounds
                    )
                    
                    // Content with MaterialTheme on top
                    DersPusulasiTheme {
                        val navController = rememberNavController()

                        NavHost(
                            navController = navController,
                            startDestination = "dashboard"
                        ) {
                            composable("dashboard") {
                                DashboardScreen(
                                    navController = navController,
                                    settingsRepository = settingsRepository
                                )
                            }

                            composable("pomodoro") {
                                PomodoroScreen(
                                    viewModel = androidx.lifecycle.viewmodel.compose.viewModel {
                                        PomodoroViewModel(settingsRepository)
                                    },
                                    onBackClick = { navController.popBackStack() }
                                )
                            }

                            composable("todo") {
                                TodoScreen(
                                    viewModel = androidx.lifecycle.viewmodel.compose.viewModel {
                                        TodoViewModel(todoRepository)
                                    },
                                    onBackClick = { navController.popBackStack() }
                                )
                            }

                            composable("countdown") {
                                CountdownScreen(
                                    onBackClick = { navController.popBackStack() }
                                )
                            }

                            composable("stopwatch") {
                                StopwatchScreen(
                                    onBackClick = { navController.popBackStack() }
                                )
                            }

                            composable("question_tracker") {
                                QuestionTrackerScreen(
                                    viewModel = androidx.lifecycle.viewmodel.compose.viewModel {
                                        QuestionTrackerViewModel(questionRepository)
                                    },
                                    onBackClick = { navController.popBackStack() }
                                )
                            }
                            
                            composable("settings") {
                                SettingsScreen(
                                    settingsRepository = settingsRepository,
                                    onBackClick = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onCreate", e)
            e.printStackTrace()
            
            // Show error screen
            setContent {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Hata Oluştu",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = e.message ?: "Bilinmeyen hata",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Lütfen Logcat'i kontrol edin.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
    
    /**
     * Applies screen brightness setting
     * @param brightness -1 for system default, 0.0-1.0 for custom brightness
     */
    private fun applyScreenBrightness(brightness: Float) {
        try {
            val layoutParams = window.attributes
            if (brightness < 0) {
                // Use system brightness
                layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
            } else {
                // Use custom brightness (clamped between 0.0 and 1.0)
                layoutParams.screenBrightness = brightness.coerceIn(0f, 1f)
            }
            window.attributes = layoutParams
        } catch (e: Exception) {
            Log.e("MainActivity", "Error applying screen brightness", e)
        }
    }
    
    /**
     * Updates screen brightness dynamically
     */
    fun updateScreenBrightness(brightness: Float) {
        applyScreenBrightness(brightness)
    }
}
