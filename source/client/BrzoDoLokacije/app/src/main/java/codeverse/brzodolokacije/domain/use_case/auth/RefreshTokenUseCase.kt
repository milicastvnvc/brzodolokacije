package codeverse.brzodolokacije.domain.use_case.auth

import codeverse.brzodolokacije.data.models.CustomResponse
import codeverse.brzodolokacije.data.models.RefreshTokenResponse
import codeverse.brzodolokacije.data.repository.AuthRepository
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.Result
import codeverse.brzodolokacije.utils.StatusCode
import codeverse.brzodolokacije.utils.managers.TokenManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.time.Duration

class RefreshTokenUseCase @Inject constructor(private val authRepository: AuthRepository) {

    @Inject
    lateinit var tokenManager: TokenManager

    operator fun invoke(period: Duration, initialDelay: Duration = Duration.ZERO) : Flow<Result<CustomResponse<RefreshTokenResponse>>> = flow {

        var refreshToken: String = ""
        delay(initialDelay)
        try{
            while (true) {
                refreshToken = tokenManager.getToken(isRefresh = true)?: ""
                val response = authRepository.refreshToken(refreshToken)
                if(response.isSuccessful){
                    if(response.body()!!.success){
                        println("USPESNO JE! JWT TOKEN JE "+ response.body()!!.actionData.jwtToken)
                        emit(Result.Success(data = response.body()!!))
                        tokenManager.saveToken(response.body()!!.actionData.jwtToken)
                        tokenManager.saveToken(response.body()!!.actionData.refreshToken, isRefresh = true)
                    }
                    else{
                        if(response.body()!!.errors.size > 0){
                            emit(Result.Error(message = response.body()!!.errors[0]))
                        }
                    }

                }
                else{
                    if (response.code() == StatusCode.Unauthorized.code){
                        emit(Result.Error(redirect = true, message = "Niste autorizovani"))
                    }
                }
                delay(period)
            }
        } catch(e: HttpException) {
            e.printStackTrace()
            emit(Result.Error(e.localizedMessage ?: Constants.UNEXPECTED_ERROR))
        } catch(e: IOException) {
            e.printStackTrace()
            emit(Result.Error(Constants.SERVER_ERROR))
        }
    }

}