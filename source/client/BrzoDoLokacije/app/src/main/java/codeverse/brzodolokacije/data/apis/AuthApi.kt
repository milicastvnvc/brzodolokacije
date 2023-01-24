package codeverse.brzodolokacije.data.apis

import codeverse.brzodolokacije.data.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("Auth/Register")
    suspend fun register(@Body registerUser: RegisterUser) : Response<CustomResponse<User>>

    @POST("Auth/Login")
    suspend fun login(@Body loginUser: LoginUser) : Response<CustomResponse<LoginResponse>>

    @POST("Auth/VerifyUser")
    suspend fun verifyUser(@Body verifyUser : VerifyUser) : Response<CustomResponse<Unit>>

    @POST("Auth/SendVerificationToken")
    suspend fun sendVerificationToken(@Body verificationCodeRequest: VerificationCodeRequest) : Response<CustomResponse<Unit>>

    @POST("Auth/ResetPassword")
    suspend fun resetPassword(@Body resetPasswordData: ResetPasswordData) : Response<CustomResponse<Unit>>

    @POST("Auth/RefreshToken")
    suspend fun refreshToken(@Query("refreshToken") refreshToken: String) : Response<CustomResponse<RefreshTokenResponse>>
}