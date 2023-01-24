package codeverse.brzodolokacije.data.models

data class UsersPostResponse(val avgRating: Double,
                             val numberOfRatings: Int,
                             val numberOfPosts: Int,
                             val posts: MutableList<PostItem>)