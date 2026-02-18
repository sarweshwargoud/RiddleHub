package com.sarweshwar.riddlehub.ui.create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarweshwar.riddlehub.data.AuthRepository
import com.sarweshwar.riddlehub.data.PuzzleRepository
import com.sarweshwar.riddlehub.model.Puzzle
import kotlinx.coroutines.launch

class CreatePuzzleViewModel : ViewModel() {
    private val puzzleRepository = PuzzleRepository()
    private val authRepository = AuthRepository()

    val loading = mutableStateOf(false)

    fun createPuzzle(
        title: String,
        description: String,
        hint: String?,
        difficulty: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val user = authRepository.getCurrentUser()
        if (user == null) {
            onResult(false, "User not logged in")
            return
        }

        loading.value = true
        viewModelScope.launch {
            val puzzle = Puzzle(
                title = title,
                description = description,
                hint = hint,
                difficulty = difficulty,
                authorId = user.uid,
                authorName = user.displayName ?: user.email?.substringBefore("@") ?: "Anonymous",
                timestamp = System.currentTimeMillis()
            )
            val result = puzzleRepository.createPuzzle(puzzle)
            result.onSuccess {
                onResult(true, null)
            }.onFailure {
                onResult(false, it.message)
            }
            loading.value = false
        }
    }
}
