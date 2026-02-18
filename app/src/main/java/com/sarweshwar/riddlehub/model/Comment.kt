package com.sarweshwar.riddlehub.model

import com.google.firebase.firestore.Exclude

data class Comment(
    val id: String = "",
    val puzzleId: String = "",
    val userId: String = "",
    val username: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    val parentCommentId: String? = null, // Used for flat-tree structure
    @get:Exclude var replies: List<Comment> = emptyList() // Populated in memory
)
