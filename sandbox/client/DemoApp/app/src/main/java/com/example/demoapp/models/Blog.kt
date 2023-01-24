package com.example.demoapp.models

data class Blog(

    val id: Long,
    val title : String? = null,
    val posts : MutableList<Post>
)
//    val userId : Int? = null,
//    val id: String? = null,
//    val title : String? = null,
//    val body : String? = null)