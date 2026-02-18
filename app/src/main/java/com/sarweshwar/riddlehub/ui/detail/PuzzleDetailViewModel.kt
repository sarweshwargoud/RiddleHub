package com.sarweshwar.riddlehub.ui.detail

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarweshwar.riddlehub.data.AuthRepository
import com.sarweshwar.riddlehub.data.CommentRepository
import com.sarweshwar.riddlehub.data.PuzzleRepository
import com.sarweshwar.riddlehub.model.Comment
import com.sarweshwar.riddlehub.model.Puzzle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PuzzleDetailViewModel : ViewModel() {
    private val puzzleRepository = PuzzleRepository()
    private val commentRepository = CommentRepository()
    private val authRepository = AuthRepository()

    val puzzle = mutableStateOf<Puzzle?>(null)
    val comments = mutableStateListOf<Comment>()
    val loading = mutableStateOf(false)

    fun loadPuzzle(puzzleId: String) {
        loading.value = true
        viewModelScope.launch {
            puzzleRepository.getPuzzles().collectLatest { list ->
                puzzle.value = list.find { it.id == puzzleId }
            }
        }
        viewModelScope.launch {
            commentRepository.getCommentsForPuzzle(puzzleId).collectLatest { list ->
                comments.clear()
                comments.addAll(list)
                loading.value = false
            }
        }
    }

    fun postComment(text: String, parentCommentId: String? = null) {
        val p = puzzle.value ?: return
        val user = authRepository.getCurrentUser() ?: return
        viewModelScope.launch {
            val comment = Comment(
                puzzleId = p.id,
                userId = user.uid,
                username = user.displayName ?: user.email?.substringBefore("@") ?: "Anonymous",
                text = text,
                timestamp = System.currentTimeMillis(),
                parentCommentId = parentCommentId
            )
            commentRepository.addComment(comment)
        }
    }

    fun toggleLike() {
        val p = puzzle.value ?: return
        val userId = authRepository.getCurrentUser()?.uid ?: return
        viewModelScope.launch {
            puzzleRepository.toggleLike(p.id, userId)
        }
    }
}
