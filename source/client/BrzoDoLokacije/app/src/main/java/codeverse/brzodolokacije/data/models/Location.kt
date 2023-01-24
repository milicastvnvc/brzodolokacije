package codeverse.brzodolokacije.data.models

data class Location(val id: Long,
                    val name: String,
                    val countryCode: String,
                    val countryName: String,
                    val latitude: Double,
                    val longitude: Double,
                    val posts: MutableList<NewPost>)

//{
//    "id": 35645,
//    "name": "Kragujevac",
//    "countryCode": "RS",
//    "countryName": "Serbia",
//    "latitude": 44.01667,
//    "longitude": 20.91667,
//    "posts": null
//}
