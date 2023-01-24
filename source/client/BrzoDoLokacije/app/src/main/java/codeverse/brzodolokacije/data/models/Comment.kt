package codeverse.brzodolokacije.data.models

data class Comment(
    val id: Long,
    val createdById: Long,
    val createdBy: User,
    val text: String,
    val postId: Long,
    val createdDate: String,
    val parentCommentId: Long? = null,
    val childrenComments: List<Comment>,
)