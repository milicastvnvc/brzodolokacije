package codeverse.brzodolokacije.data.models

data class PostItem(
    val id: Long,
    val description: String,
    val dateCreated: String,
    val placeName: String,
    val longitudeCenter: Double,
    val latitudeCenter : Double,
    val text: String,
    val placeNameSr: String,
    val createdBy: User,
    val avgRating : Double,
    val numberOfRatings: Int,
    val numberOfComments:Int,
    val userRate: Int?,
    val resources: List<Resource>,
    val tags: List<String>,
    val comments: List<Comment>)