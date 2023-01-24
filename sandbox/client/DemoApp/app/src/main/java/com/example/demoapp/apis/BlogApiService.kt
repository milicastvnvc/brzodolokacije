package com.example.demoapp.apis

import com.example.demoapp.models.Blog
import com.example.demoapp.models.CustomResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BlogApiService {

    @GET("Blogs")
    fun getBlogs() : Call<MutableList<Blog>>

    @POST("Blogs")
    fun addBlog(@Body blog: Blog) : Call<CustomResponse>
}