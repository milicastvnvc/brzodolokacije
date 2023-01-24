package codeverse.brzodolokacije.domain.use_case.auth

import codeverse.brzodolokacije.data.models.VerificationCodeRequest
import codeverse.brzodolokacije.data.models.VerifyUser
import codeverse.brzodolokacije.data.repository.AuthRepository
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class VerificationUseCase @Inject constructor(private val authRepository: AuthRepository) {

    operator fun invoke(verifyUser : VerifyUser): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading())

            val response = authRepository.verifyUser(verifyUser)

            if (response.isSuccessful){

                if (response.body() != null){

                    if (response.body()!!.success){
                        emit(Result.Success<Unit>(response.body()!!.actionData))
                    }
                    else {
                        if (response.body()!!.errors != null) {
                            emit(Result.Error<Unit>(response.body()!!.errors.first()))

                        }
                    }

                }
            }
            else{
                emit(Result.Error<Unit>( "Nije uspešno"))
            }
        } catch(e: HttpException) {
            emit(Result.Error<Unit>(e.localizedMessage ?: Constants.UNEXPECTED_ERROR))
        } catch(e: IOException) {
            emit(Result.Error<Unit>(Constants.SERVER_ERROR))
        }
    }
}