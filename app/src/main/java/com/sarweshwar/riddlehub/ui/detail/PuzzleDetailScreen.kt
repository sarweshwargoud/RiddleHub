package com.sarweshwar.riddlehub.ui.detail

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sarweshwar.riddlehub.components.CommentItem
import com.sarweshwar.riddlehub.components.DifficultyBadge
import com.sarweshwar.riddlehub.components.GradientBackground
import com.sarweshwar.riddlehub.components.LikeButton
import com.sarweshwar.riddlehub.model.Comment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuzzleDetailScreen(puzzleId: String, onBackClick: () -> Unit) {
    val viewModel: PuzzleDetailViewModel = viewModel()
    val puzzle by viewModel.puzzle
    val comments by remember { derivedStateOf { viewModel.comments } }
    val loading by viewModel.loading

    var commentText by remember { mutableStateOf("") }
    var replyingTo by remember { mutableStateOf<Comment?>(null) }
    var showHint by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(puzzleId) {
        viewModel.loadPuzzle(puzzleId)
    }

    GradientBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Details", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            bottomBar = {
                Surface(color = Color(0xFF1E1E1E), tonalElevation = 8.dp) {
                    Column {
                        if (replyingTo != null) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Replying to @${replyingTo!!.username}", color = Color.Gray, fontSize = 12.sp)
                                TextButton(onClick = { replyingTo = null }) {
                                    Text("Cancel", color = Color(0xFF6C63FF))
                                }
                            }
                        }
                        OutlinedTextField(
                            value = commentText,
                            onValueChange = { commentText = it },
                            placeholder = { Text(if (replyingTo == null) "Add a comment..." else "Add a reply...", color = Color.Gray) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(24.dp),
                            trailingIcon = {
                                if (commentText.isNotBlank()) {
                                    IconButton(onClick = {
                                        viewModel.postComment(commentText, replyingTo?.id)
                                        commentText = ""
                                        replyingTo = null
                                    }) {
                                        Icon(Icons.Default.Send, contentDescription = "Send", tint = Color(0xFF6C63FF))
                                    }
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF6C63FF),
                                unfocusedBorderColor = Color.Gray,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    }
                }
            }
        ) { paddingValues ->
            if (loading && puzzle == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF6C63FF))
                }
            } else if (puzzle != null) {
                val p = puzzle!!
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 20.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "@${p.authorName}",
                                style = MaterialTheme.typography.labelLarge.copy(color = Color(0xFF6C63FF))
                            )
                            DifficultyBadge(difficulty = p.difficulty)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = p.title,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = p.description,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color.LightGray,
                                lineHeight = 24.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        if (p.hint != null) {
                            Button(
                                onClick = { showHint = !showHint },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(if (showHint) "Hide Hint" else "Show Hint", color = Color.White)
                            }

                            AnimatedVisibility(visible = showHint) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = p.hint,
                                        modifier = Modifier.padding(16.dp),
                                        color = Color(0xFFFF6584),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LikeButton(isLiked = p.isLiked) { viewModel.toggleLike() }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("${p.likesCount}", color = Color.White)

                            Spacer(modifier = Modifier.weight(1f))

                            IconButton(onClick = { Toast.makeText(context, "Shared!", Toast.LENGTH_SHORT).show() }) {
                                Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.Gray)
                            }
                        }

                        HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f))

                        Text(
                            text = "Comments (${comments.sumOf { 1 + it.replies.size }})",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }

                    items(comments) { comment ->
                        CommentItem(comment = comment) { selectedComment ->
                            replyingTo = selectedComment
                        }
                    }
                }
            }
        }
    }
}
