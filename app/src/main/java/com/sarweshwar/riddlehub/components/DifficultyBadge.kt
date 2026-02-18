package com.sarweshwar.riddlehub.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DifficultyBadge(difficulty: String) {
    val (backgroundColor, textColor) = when (difficulty.lowercase()) {
        "easy" -> Color(0xFFC8E6C9) to Color(0xFF2E7D32)
        "medium" -> Color(0xFFFFF9C4) to Color(0xFFF57F17)
        "hard" -> Color(0xFFFFCDD2) to Color(0xFFC62828)
        "expert" -> Color(0xFFE1BEE7) to Color(0xFF6A1B9A)
        else -> Color.Gray to Color.White
    }

    Text(
        text = difficulty.uppercase(),
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        color = textColor,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold
    )
}
