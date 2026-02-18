package com.sarweshwar.riddlehub.model

import com.google.firebase.firestore.Exclude

data class Puzzle(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val hint: String? = null,
    val difficulty: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val timestamp: Long = 0,
    val likesCount: Int = 0,
    @get:Exclude var isLiked: Boolean = false
)
