package com.sarweshwar.riddlehub.data

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sarweshwar.riddlehub.model.Puzzle
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PuzzleRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun createPuzzle(puzzle: Puzzle): Result<Unit> {
        return try {
            db.collection("puzzles").add(puzzle).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getPuzzles(): Flow<List<Puzzle>> = callbackFlow {
        val listener = db.collection("puzzles")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val puzzles = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Puzzle::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                trySend(puzzles)
            }
        awaitClose { listener.remove() }
    }

    suspend fun toggleLike(puzzleId: String, userId: String): Result<Unit> {
        return try {
            val puzzleRef = db.collection("puzzles").document(puzzleId)
            val likeRef = puzzleRef.collection("likes").document(userId)

            db.runTransaction { transaction ->
                val snapshot = transaction.get(likeRef)
                if (snapshot.exists()) {
                    transaction.delete(likeRef)
                    transaction.update(puzzleRef, "likesCount", FieldValue.increment(-1))
                } else {
                    transaction.set(likeRef, mapOf("userId" to userId))
                    transaction.update(puzzleRef, "likesCount", FieldValue.increment(1))
                }
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isLiked(puzzleId: String, userId: String): Boolean {
        return try {
            val likeDoc = db.collection("puzzles").document(puzzleId)
                .collection("likes").document(userId).get().await()
            likeDoc.exists()
        } catch (e: Exception) {
            false
        }
    }
}
