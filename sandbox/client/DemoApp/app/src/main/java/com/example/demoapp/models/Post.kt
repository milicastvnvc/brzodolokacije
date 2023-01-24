package com.example.demoapp.models

data class Post(val id: Long,
                val title : String? = null,
                val text : String? = null,
                val author : String? = null,
                val blogId : Long,
                val blog : Blog? = null)