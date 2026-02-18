package com.sarweshwar.riddlehub.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color

@Composable
fun LikeButton(
    isLiked: Boolean,
    onLikeClick: (Boolean) -> Unit
) {
    val transition = updateTransition(targetState = isLiked, label = "LikeTransition")

    val iconColor by transition.animateColor(
        transitionSpec = { tween(durationMillis = 300) },
        label = "LikeColor"
    ) { liked ->
        if (liked) Color.Red else Color.Gray
    }

    val scale by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300) },
        label = "LikeScale"
    ) { liked ->
        if (liked) 1.2f else 1.0f
    }

    Icon(
        imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
        contentDescription = "Like",
        tint = iconColor,
        modifier = Modifier
            .scale(scale)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onLikeClick(!isLiked) }
    )
}
