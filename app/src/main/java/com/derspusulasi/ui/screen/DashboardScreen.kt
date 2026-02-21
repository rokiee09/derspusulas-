package com.derspusulasi.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.derspusulasi.R
import androidx.navigation.NavController
import com.derspusulasi.ui.components.MotivationCard
import com.derspusulasi.data.repository.SettingsRepository

data class DashboardCard(
    val title: String,
    val iconResId: Int, // PNG icon resource ID
    val route: String,
    val backgroundColor: androidx.compose.ui.graphics.Color
)

@Composable
fun DashboardScreen(
    navController: NavController,
    settingsRepository: SettingsRepository,
    modifier: Modifier = Modifier
) {
    // Background is handled in MainActivity, no need to duplicate here
    val cards = listOf(
        DashboardCard(
            title = "Pomodoro",
            iconResId = R.drawable.pomodoro,
            route = "pomodoro",
            backgroundColor = Color(0xFFFFF5E6) // Soft cream/beige
        ),
        DashboardCard(
            title = "Yapılacaklar",
            iconResId = R.drawable.yaplacaklar,
            route = "todo",
            backgroundColor = Color(0xFFF0F8FF) // Soft light blue
        ),
        DashboardCard(
            title = "Geri Sayım",
            iconResId = R.drawable.geri_saym,
            route = "countdown",
            backgroundColor = Color(0xFFF5F0FF) // Soft lavender
        ),
        DashboardCard(
            title = "Kronometre",
            iconResId = R.drawable.kronometre,
            route = "stopwatch",
            backgroundColor = Color(0xFFFFF0F5) // Soft pink
         ),
        DashboardCard(
            title = "Soru Takibi",
            iconResId = R.drawable.soru_takibi,
            route = "question_tracker",
            backgroundColor = Color(0xFFF0FFF0) // Soft mint green
        )
    )

    // Animation for app name
    val infiniteTransition = rememberInfiniteTransition(label = "app_name_animation")
    val appNameAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "app_name_alpha"
    )
    val appNameScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "app_name_scale"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
                // App Header with Logo, Animated Name, and Settings Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(48.dp)) // Balance for settings button
                    
                    // Center: Logo and Name
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // App Logo
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "DersPusulası Logo",
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        // Animated App Name
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black, // Siyah renk
                            modifier = Modifier
                                .alpha(appNameAlpha)
                                .graphicsLayer {
                                    scaleX = appNameScale
                                    scaleY = appNameScale
                                }
                        )
                    }
                    
                    // Settings Button
                    IconButton(
                        onClick = { navController.navigate("settings") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Ayarlar",
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Motivation Card
                MotivationCard()

                Spacer(modifier = Modifier.height(8.dp))

                // Module Cards
                cards.chunked(2).forEach { rowCards ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        rowCards.forEach { card ->
                            DashboardCardItem(
                                card = card,
                                onClick = { navController.navigate(card.route) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // Fill empty space if odd number
                        if (rowCards.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
}

@Composable
fun DashboardCardItem(
    card: DashboardCard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "card_scale"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                },
                indication = null,
                interactionSource = interactionSource
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Large Icon - Outside the card, no background
        Image(
            painter = painterResource(id = card.iconResId),
            contentDescription = card.title,
            modifier = Modifier
                .size(120.dp)
                .scale(scale),
            contentScale = ContentScale.Fit
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Module Name Below Icon - Ensure visibility
        Text(
            text = card.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black, // Explicit black color for better visibility
            modifier = Modifier.scale(scale),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
