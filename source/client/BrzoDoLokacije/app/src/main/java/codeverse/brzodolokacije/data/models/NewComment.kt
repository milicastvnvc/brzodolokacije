package codeverse.brzodolokacije.data.models

data class NewComment(val postId: Long, val text: String, val parentCommentId: Long? = null)
