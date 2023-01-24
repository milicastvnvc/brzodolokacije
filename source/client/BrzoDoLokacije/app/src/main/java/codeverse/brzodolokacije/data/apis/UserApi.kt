package codeverse.brzodolokacije.data.apis

import codeverse.brzodolokacije.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    @POST("User")
    suspend fun editUser(@Body updatedUser: UpdatedUser) : Response<CustomResponse<User>>

    @POST("User/LikeUser")
    suspend fun likeUser(@Body userId: Long) : Response<CustomResponse<Unit>>

    @POST("User/DislikeUser")
    suspend fun dislikeUser(@Body userId: Long) : Response<CustomResponse<Unit>>

    @GET("User/GetLikedUsers")
    suspend fun getLikedUsers(@Query("pageIndex") pageIndex: Int? = null,
                              @Query("pageSize") pageSize: Int? = null,
                              @Query("searchTerm") searchTerm: String? = null) : Response<MutableList<User>>


    @GET("User/{userId}")
    suspend fun getUserById(@Path("userId") userId: Long) : Response<CustomResponse<User>>

}