package com.sarweshwar.riddlehub.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sarweshwar.riddlehub.model.Comment
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class CommentRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun addComment(comment: Comment): Result<Unit> {
        return try {
            db.collection("comments").add(comment).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCommentsForPuzzle(puzzleId: String): Flow<List<Comment>> = callbackFlow {
        val listener = db.collection("comments")
            .whereEqualTo("puzzleId", puzzleId)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val comments = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Comment::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                // In-memory reconstruction of the comment tree
                val commentMap = comments.associateBy { it.id }
                val topLevelComments = comments.filter { it.parentCommentId == null }.map { comment ->
                    comment.copy(replies = comments.filter { it.parentCommentId == comment.id })
                }

                trySend(topLevelComments)
            }
        awaitClose { listener.remove() }
    }
}
