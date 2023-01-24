package codeverse.brzodolokacije.data.models


data class NewPost(val description: String?,
                   val longitudeCenter: Double,
                   val latitudeCenter: Double,
                   val placeName: String,
                   val text: String,
                   val placeNameSr: String,
                   val tags: MutableList<String>)