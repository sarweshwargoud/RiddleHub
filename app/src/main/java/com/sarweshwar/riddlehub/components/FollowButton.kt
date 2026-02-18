package com.sarweshwar.riddlehub.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FollowButton(
    isFollowing: Boolean,
    onFollowClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        if (isFollowing) Color.Gray else Color(0xFF6C63FF),
        label = "FollowButtonColor"
    )
    
    val scale by animateFloatAsState(
        if (isFollowing) 0.95f else 1f,
        label = "FollowButtonScale"
    )

    Button(
        onClick = onFollowClick,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .scale(scale)
            .padding(horizontal = 8.dp)
    ) {
        Text(text = if (isFollowing) "Following" else "Follow")
    }
}
