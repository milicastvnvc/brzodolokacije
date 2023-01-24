package codeverse.brzodolokacije.domain.use_case.auth

import codeverse.brzodolokacije.data.models.RegisterUser
import codeverse.brzodolokacije.data.models.User
import codeverse.brzodolokacije.data.repository.AuthRepository
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    operator fun invoke(registerUser :RegisterUser): Flow<Result<User>> = flow {
        //println("POZVAO SE!")
        try {
            emit(Result.Loading())

            val response = authRepository.registerUser(registerUser)

            if (response.isSuccessful){

                if (response.body() != null){

                    if (response.body()!!.success){
                        emit(Result.Success<User>(response.body()!!.actionData))
                    }
                    else {
                        if (response.body()!!.errors != null) {
                            emit(Result.Error<User>(response.body()!!.errors.first()))

                        }

                    }

                }

            }
            else{
                emit(Result.Error<User>( "Nije uspešno"))
            }
        } catch(e: HttpException) {
            emit(Result.Error<User>(e.localizedMessage ?: Constants.UNEXPECTED_ERROR))
        } catch(e: IOException) {
            emit(Result.Error<User>(Constants.SERVER_ERROR))
        }
    }
}