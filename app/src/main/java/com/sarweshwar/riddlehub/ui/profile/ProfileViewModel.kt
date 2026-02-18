package com.sarweshwar.riddlehub.ui.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarweshwar.riddlehub.data.AuthRepository
import com.sarweshwar.riddlehub.data.PuzzleRepository
import com.sarweshwar.riddlehub.data.UserRepository
import com.sarweshwar.riddlehub.model.Puzzle
import com.sarweshwar.riddlehub.model.User
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()
    private val puzzleRepository = PuzzleRepository()

    val user = mutableStateOf<User?>(null)
    val puzzles = mutableStateOf<List<Puzzle>>(emptyList())
    val isFollowing = mutableStateOf(false)
    val loading = mutableStateOf(false)

    fun loadProfile(userId: String) {
        loading.value = true
        viewModelScope.launch {
            user.value = userRepository.getUser(userId)
            loadUserPuzzles(userId)
            checkIfFollowing(userId)
            loading.value = false
        }
    }

    private fun loadUserPuzzles(userId: String) {
        viewModelScope.launch {
            puzzleRepository.getPuzzles().collectLatest { allPuzzles ->
                puzzles.value = allPuzzles.filter { it.authorId == userId }
            }
        }
    }

    private suspend fun checkIfFollowing(profileUserId: String) {
        val currentUserId = authRepository.getCurrentUser()?.uid ?: return
        isFollowing.value = userRepository.isFollowing(currentUserId, profileUserId)
    }

    fun toggleFollow(profileUserId: String) {
        val currentUserId = authRepository.getCurrentUser()?.uid ?: return
        viewModelScope.launch {
            userRepository.toggleFollow(currentUserId, profileUserId)
            checkIfFollowing(profileUserId) // Refresh following state
            loadProfile(profileUserId) // Refresh counts
        }
    }
}
