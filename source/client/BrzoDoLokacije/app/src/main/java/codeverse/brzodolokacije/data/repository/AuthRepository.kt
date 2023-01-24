package codeverse.brzodolokacije.data.repository

import android.app.Application
import android.util.Log
import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.data.apis.AuthApi
import codeverse.brzodolokacije.data.models.*
import codeverse.brzodolokacije.utils.Constants
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi : AuthApi) {

    suspend fun registerUser(registerUser : RegisterUser): Response<CustomResponse<User>> {
        return authApi.register(registerUser)
    }

    suspend fun login(loginUser : LoginUser): Response<CustomResponse<LoginResponse>> {
        return authApi.login(loginUser)
    }

    suspend fun verifyUser(verifyUser: VerifyUser): Response<CustomResponse<Unit>> {
        return authApi.verifyUser(verifyUser)
    }

    suspend fun sendVerificationToken(verificationCodeRequest: VerificationCodeRequest) : Response<CustomResponse<Unit>>{
        return authApi.sendVerificationToken(verificationCodeRequest)
    }

    suspend fun resetPassword(resetPasswordData: ResetPasswordData) : Response<CustomResponse<Unit>>{
        return authApi.resetPassword(resetPasswordData)
    }

    suspend fun refreshToken(refreshToken: String) : Response<CustomResponse<RefreshTokenResponse>>{
        return authApi.refreshToken(refreshToken)
    }

}