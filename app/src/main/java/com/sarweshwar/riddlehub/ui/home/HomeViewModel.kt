package com.sarweshwar.riddlehub.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarweshwar.riddlehub.data.AuthRepository
import com.sarweshwar.riddlehub.data.PuzzleRepository
import com.sarweshwar.riddlehub.model.Puzzle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val puzzleRepository = PuzzleRepository()
    private val authRepository = AuthRepository()

    private val _puzzles = MutableStateFlow<List<Puzzle>>(emptyList())
    val puzzles = _puzzles.asStateFlow()

    init {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUser()?.uid
            puzzleRepository.getPuzzles().collectLatest { puzzleList ->
                if (userId != null) {
                    // Launch a separate coroutine for each puzzle to avoid blocking
                    puzzleList.forEach { puzzle ->
                        viewModelScope.launch {
                            puzzle.isLiked = puzzleRepository.isLiked(puzzle.id, userId)
                        }
                    }
                }
                _puzzles.value = puzzleList
            }
        }
    }

    fun toggleLike(puzzleId: String) {
        val userId = authRepository.getCurrentUser()?.uid ?: return
        viewModelScope.launch {
            puzzleRepository.toggleLike(puzzleId, userId)
        }
    }
}
