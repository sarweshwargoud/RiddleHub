package com.sarweshwar.riddlehub.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sarweshwar.riddlehub.model.Comment

@Composable
fun CommentItem(
    comment: Comment,
    depth: Int = 0,
    onReplyClick: (Comment) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = (depth * 20).dp, top = 8.dp, bottom = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            // Placeholder Avatar
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF6C63FF).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF6C63FF)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column {
                Text(
                    text = "@${comment.username}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6C63FF)
                    )
                )
                Text(
                    text = comment.text,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "2h", // Mock time
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    TextButton(
                        onClick = { onReplyClick(comment) },
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.height(20.dp)
                    ) {
                        Text("Reply", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Recursively render replies
        comment.replies.forEach { reply ->
            CommentItem(comment = reply, depth = depth + 1, onReplyClick = onReplyClick)
        }
    }
}
