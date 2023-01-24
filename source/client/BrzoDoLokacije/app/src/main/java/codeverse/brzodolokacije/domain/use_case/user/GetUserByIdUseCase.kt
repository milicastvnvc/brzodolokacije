package codeverse.brzodolokacije.domain.use_case.user

import codeverse.brzodolokacije.data.models.User
import codeverse.brzodolokacije.data.repository.UserRepository
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(private val userRepository: UserRepository) {

    operator fun invoke(userId: Long) : Flow<Result<User>> = flow {
        try {

            emit(Result.Loading())
            val response = userRepository.getUserById(userId)

            if (response.isSuccessful) {
                if (response.body() != null) {
                    if(response.body()!!.success){
                        emit(Result.Success(response.body()!!.actionData))
                    }
                    else{
                        if (response.body()!!.errors.size > 0){
                            emit(Result.Error(response.body()!!.errors.first()))
                        }
                    }

                }
            }
            else{
                println(response.message())
                println(response.code())
                emit(Result.Error("Došlo je do greške"))
            }
        } catch (e: HttpException) {
            emit(Result.Error(e.localizedMessage ?: Constants.UNEXPECTED_ERROR))
        } catch (e: IOException) {
            emit(Result.Error(Constants.SERVER_ERROR))
        }
    }
}