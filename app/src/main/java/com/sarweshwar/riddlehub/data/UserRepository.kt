package com.sarweshwar.riddlehub.data

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.sarweshwar.riddlehub.model.User
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    suspend fun getUser(uid: String): User? {
        return try {
            usersCollection.document(uid).get().await().toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun toggleFollow(currentUserId: String, profileUserId: String): Result<Unit> {
        return try {
            val currentUserRef = usersCollection.document(currentUserId)
            val profileUserRef = usersCollection.document(profileUserId)

            val followingRef = currentUserRef.collection("following").document(profileUserId)
            val followersRef = profileUserRef.collection("followers").document(currentUserId)

            db.runTransaction { transaction ->
                val isFollowing = transaction.get(followingRef).exists()

                if (isFollowing) {
                    // Unfollow
                    transaction.delete(followingRef)
                    transaction.delete(followersRef)
                    transaction.update(currentUserRef, "followingCount", FieldValue.increment(-1))
                    transaction.update(profileUserRef, "followersCount", FieldValue.increment(-1))
                } else {
                    // Follow
                    transaction.set(followingRef, mapOf("uid" to profileUserId))
                    transaction.set(followersRef, mapOf("uid" to currentUserId))
                    transaction.update(currentUserRef, "followingCount", FieldValue.increment(1))
                    transaction.update(profileUserRef, "followersCount", FieldValue.increment(1))
                }
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isFollowing(currentUserId: String, profileUserId: String): Boolean {
        return try {
            usersCollection.document(currentUserId).collection("following").document(profileUserId).get().await().exists()
        } catch (e: Exception) {
            false
        }
    }
}
