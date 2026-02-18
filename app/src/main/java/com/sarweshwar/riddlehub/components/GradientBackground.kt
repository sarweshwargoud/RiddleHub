package com.sarweshwar.riddlehub.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun GradientBackground(content: @Composable () -> Unit) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1A1A2E),
            Color(0xFF16213E),
            Color(0xFF0F3460)
        )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        content()
    }
}
