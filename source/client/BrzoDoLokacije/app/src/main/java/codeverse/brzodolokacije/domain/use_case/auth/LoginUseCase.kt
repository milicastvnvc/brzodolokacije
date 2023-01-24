package codeverse.brzodolokacije.domain.use_case.auth

import codeverse.brzodolokacije.data.models.LoginResponse
import codeverse.brzodolokacije.data.models.LoginUser
import codeverse.brzodolokacije.data.repository.AuthRepository
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.Result
import codeverse.brzodolokacije.utils.managers.TokenManager
import codeverse.brzodolokacije.utils.managers.UserManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var userManager: UserManager

    operator fun invoke(loginUser : LoginUser): Flow<Result<LoginResponse>> = flow {
        try {
            emit(Result.Loading())
            val loginResponse = authRepository.login(loginUser)

            if (loginResponse.isSuccessful){
                if (loginResponse.body() != null){

                    if (!loginResponse.body()!!.hasErrors){
                        tokenManager.saveToken(loginResponse.body()!!.actionData.token)
                        tokenManager.saveToken(loginResponse.body()!!.actionData.refreshToken,isRefresh = true)
                        userManager.saveUser(loginResponse.body()!!.actionData.user)
                        emit(Result.Success<LoginResponse>(loginResponse.body()!!.actionData))
                    }
                    else{
                        if (loginResponse.body()!!.actionData != null){
                            if(!loginResponse.body()!!.actionData.user.isVerified) {
                                emit(Result.Success<LoginResponse>(loginResponse.body()!!.actionData))
                            }
                        }
                        else{
                            emit(Result.Error<LoginResponse>(loginResponse.body()!!.errors.first()))
                        }

                    }

                }

            }
            else{
                emit(Result.Error<LoginResponse>( "Nije uspešno"))
            }
        } catch(e: HttpException) {
            emit(Result.Error<LoginResponse>(e.localizedMessage ?: Constants.UNEXPECTED_ERROR))
        } catch(e: IOException) {
            emit(Result.Error<LoginResponse>(Constants.SERVER_ERROR))
        }
    }
}