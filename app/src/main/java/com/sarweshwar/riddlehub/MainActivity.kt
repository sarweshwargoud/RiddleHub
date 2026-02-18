package com.sarweshwar.riddlehub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sarweshwar.riddlehub.navigation.NavGraph
import com.sarweshwar.riddlehub.navigation.Screen
import kotlinx.coroutines.delay
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            RiddleHubTheme {
                var showSplash by remember { mutableStateOf(true) }

                if (showSplash) {
                    SplashScreen(onAnimationFinished = { showSplash = false })
                } else {
                    MainContent()
                }
            }
        }
    }
}

@Composable
fun RiddleHubTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF6C63FF),
            secondary = Color(0xFFFF6584),
            background = Color(0xFF121212),
            surface = Color(0xFF1E1E1E)
        ),
        content = content
    )
}

@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {
    val scale = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1.2f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
        delay(1000)
        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = "Brain Logo",
                tint = Color(0xFF6C63FF),
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale.value)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "RiddleHub",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6C63FF),
                    letterSpacing = 4.sp
                ),
                modifier = Modifier.scale(scale.value)
            )
        }
    }
}

@Composable
fun MainContent() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(Screen.Home.route, Screen.Create.route, Screen.Profile.route)) {
                NavigationBar(
                    containerColor = Color(0xFF1E1E1E),
                    tonalElevation = 8.dp
                ) {
                    val items = listOf(
                        Triple(Screen.Home.route, Icons.Default.Home, "Home"),
                        Triple(Screen.Create.route, Icons.Default.AddCircle, "Create"),
                        Triple(Screen.Profile.route, Icons.Default.Person, "Profile")
                    )

                    items.forEach { (route, icon, label) ->
                        NavigationBarItem(
                            selected = currentRoute == route,
                            onClick = {
                                if (currentRoute != route) {
                                    navController.navigate(route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    icon,
                                    contentDescription = label,
                                    tint = if (currentRoute == route) Color(0xFF6C63FF) else Color.Gray
                                )
                            },
                            label = {
                                Text(
                                    label,
                                    color = if (currentRoute == route) Color(0xFF6C63FF) else Color.Gray
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavGraph(navController = navController)
        }
    }
}
