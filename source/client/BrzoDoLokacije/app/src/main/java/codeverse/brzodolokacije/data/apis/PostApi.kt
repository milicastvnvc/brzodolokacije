package codeverse.brzodolokacije.data.apis

import codeverse.brzodolokacije.data.models.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface PostApi {

    @POST("Post/CreateNewPost")
    suspend fun addPost(@Body newPost: NewPost) : Response<CustomResponse<Long>>

    @Headers("Accept: multipart/form-data")
    @Multipart
    @POST("Post/UploadImages/{postId}")
    suspend fun uploadImages(@Path("postId") postId: Long, @Part files: List<MultipartBody.Part>) : Response<CustomResponse<Unit>>

    @Multipart
    @POST("Post/UploadImages/{postId}")
    suspend fun postImages(@Path("postId") postId: Long = 1, @Part images: Array<MultipartBody.Part?>) : Response<CustomResponse<Long?>>

    @GET("Post/GetPost/{postId}")
    suspend fun getPostById(@Path("postId") postId: Long) : Response<CustomResponse<PostItem>>

    @GET("Post/{userId}")
    suspend fun getPostsByUser(@Path("userId") userId: Long, @Query("pageIndex") pageIndex: Int = 0, @Query("pageSize") pageSize: Int = 20) : Response<UsersPostResponse>

    @GET("Post")
    suspend fun getPostsBySearch(@Query("placeName") placeName: String,
                                 @Query("Longitude") longitude: Double? = null,
                                 @Query("Latitude") latitude: Double? = null,
                                 @Query("Tags") tags: MutableList<String>? = null,
                                 @Query("SortBy") sortBy: Int = 0,
                                 @Query("SortOrder") sortOrder: Int = 1,
                                 @Query("pageIndex") pageIndex: Int = 0,
                                 @Query("pageSize") pageSize: Int = 20
                                 ) : Response<MutableList<PostItem>>

    @DELETE("Post/{postId}")
    suspend fun deletePost(@Path("postId") postId: Long) : Response<CustomResponse<Unit>>

    @GET("Post/GetUserPostsCoordinates/{userId}")
    suspend fun getCoordinatesByUser(@Path("userId") userId: Long) : Response<ArrayList<UserCoordinateResponse>>

    @POST("Post/AddComment")
    suspend fun addComment(@Body newComment: NewComment) : Response<CustomResponse<Unit>>

    @DELETE("Post/DeleteComment/{commentId}")
    suspend fun deleteComment(@Path("commentId") commentId: Long) : Response<CustomResponse<Unit>>

    @POST("Post/UpdateComment")
    suspend fun updateComment(@Body updatedComment: UpdatedComment) : Response<CustomResponse<Unit>>

    @POST("Post/RatePost")
    suspend fun addRate(@Body ratePost: RatePost) : Response<CustomResponse<Double>>

    @DELETE("Post/RatePost/{postId}")
    suspend fun deleteRate(@Path("postId") postId: Long) : Response<CustomResponse<Double>>

}