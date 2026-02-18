package com.sarweshwar.riddlehub.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sarweshwar.riddlehub.components.GradientBackground
import com.sarweshwar.riddlehub.components.PuzzleCard

@Composable
fun HomeScreen(onPuzzleClick: (String) -> Unit) {
    val viewModel: HomeViewModel = viewModel()
    val puzzles by viewModel.puzzles.collectAsStateWithLifecycle()

    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "RiddleHub",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = 2.sp
                )
            )
            Text(
                text = "Unravel the mysteries",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(puzzles) { puzzle ->
                    PuzzleCard(
                        puzzle = puzzle,
                        onCardClick = { onPuzzleClick(puzzle.id) },
                        onLikeClick = { viewModel.toggleLike(puzzle.id) }
                    )
                }
            }
        }
    }
}
