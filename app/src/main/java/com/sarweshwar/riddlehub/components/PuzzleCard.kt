package com.sarweshwar.riddlehub.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sarweshwar.riddlehub.model.Puzzle

@Composable
fun PuzzleCard(
    puzzle: Puzzle,
    onCardClick: () -> Unit,
    onLikeClick: () -> Unit = {}
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "@${puzzle.authorName}",
                    style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF6C63FF))
                )
                DifficultyBadge(difficulty = puzzle.difficulty)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = puzzle.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = puzzle.description,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LikeButton(isLiked = puzzle.isLiked) { onLikeClick() }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${puzzle.likesCount}",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Comments",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(onClick = { Toast.makeText(context, "Shared!", Toast.LENGTH_SHORT).show() }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
