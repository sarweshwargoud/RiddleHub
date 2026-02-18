package com.sarweshwar.riddlehub.model

data class User(
    val uid: String = "",
    val email: String = "",
    val username: String = "",
    val followersCount: Int = 0,
    val followingCount: Int = 0
)
