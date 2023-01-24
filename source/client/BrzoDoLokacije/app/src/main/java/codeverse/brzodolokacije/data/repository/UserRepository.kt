package codeverse.brzodolokacije.data.repository

import codeverse.brzodolokacije.data.apis.UserApi
import codeverse.brzodolokacije.data.models.*
import retrofit2.Response
import retrofit2.http.Path
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {

    suspend fun editUser(updatedUser: UpdatedUser) : Response<CustomResponse<User>> {
        return userApi.editUser(updatedUser)
    }

    suspend fun likeUser(userId: Long): Response<CustomResponse<Unit>>{
        return userApi.likeUser(userId)
    }

    suspend fun dislikeUser(userId: Long): Response<CustomResponse<Unit>>{
        return userApi.dislikeUser(userId)
    }
    suspend fun getLikedUsers(pageIndex: Int? = null, pageSize: Int? = null, searchTerm: String? = null): Response<MutableList<User>>{
        return userApi.getLikedUsers(pageIndex,pageSize,searchTerm)
    }

    suspend fun getUserById(userId: Long) : Response<CustomResponse<User>> {
        return userApi.getUserById(userId)
    }

}