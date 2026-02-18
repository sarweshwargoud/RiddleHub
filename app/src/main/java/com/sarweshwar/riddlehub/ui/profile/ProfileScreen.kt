package com.sarweshwar.riddlehub.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sarweshwar.riddlehub.components.FollowButton
import com.sarweshwar.riddlehub.components.GradientBackground
import com.sarweshwar.riddlehub.components.PuzzleCard
import com.sarweshwar.riddlehub.data.AuthRepository

@Composable
fun ProfileScreen(
    onPuzzleClick: (String) -> Unit,
    onLogout: () -> Unit,
    userId: String? = null // Nullable for viewing other profiles
) {
    val viewModel: ProfileViewModel = viewModel()
    val authRepository = AuthRepository()
    val profileUserId = userId ?: authRepository.getCurrentUser()?.uid ?: ""

    val user by viewModel.user
    val puzzles by viewModel.puzzles
    val isFollowing by viewModel.isFollowing
    val loading by viewModel.loading

    LaunchedEffect(profileUserId) {
        viewModel.loadProfile(profileUserId)
    }

    GradientBackground {
        if (loading && user == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF6C63FF))
            }
        } else if (user != null) {
            val u = user!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6C63FF).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(50.dp),
                            tint = Color(0xFF6C63FF)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = u.username,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = u.email,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProfileStat("Puzzles", puzzles.size.toString())
                        ProfileStat("Followers", u.followersCount.toString())
                        ProfileStat("Following", u.followingCount.toString())
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (profileUserId != authRepository.getCurrentUser()?.uid) {
                        FollowButton(isFollowing = isFollowing) {
                            viewModel.toggleFollow(profileUserId)
                        }
                    } else {
                        Button(onClick = onLogout) {
                            Text("Logout")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF1E1E1E).copy(alpha = 0.5f),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "Puzzles",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            items(puzzles) { puzzle ->
                                PuzzleCard(puzzle = puzzle, onCardClick = { onPuzzleClick(puzzle.id) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
        )
    }
}
