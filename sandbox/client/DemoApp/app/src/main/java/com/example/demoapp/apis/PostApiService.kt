package com.example.demoapp.apis

import com.example.demoapp.models.CustomResponse
import com.example.demoapp.models.DeleteResponse
import com.example.demoapp.models.Post
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PostApiService {

    @GET("Posts/GetByBlogId/{id}")
    fun getPostByBlog(@Path("id") blogId : Long) : Call<MutableList<Post>>

    @POST("Posts")
    fun addPost(@Body post: Post) : Call<CustomResponse>

    @DELETE("Posts/{id}")
    fun deletePost(@Path("id") postId : Long) : Call<DeleteResponse>
}