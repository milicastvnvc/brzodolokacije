package codeverse.brzodolokacije.data.models

data class User(val id: Long = 0,
                val username: String,
                val email: String,
                val firstName: String,
                val lastName: String,
                val avgRating: Double,
                val profilePicturePath: String? = "",
                val isLikedByCurrentUser: Boolean,
                val isVerified : Boolean)
