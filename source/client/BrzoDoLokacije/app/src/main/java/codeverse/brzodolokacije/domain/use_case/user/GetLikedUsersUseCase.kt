package codeverse.brzodolokacije.domain.use_case.user

import codeverse.brzodolokacije.data.models.CustomResponse
import codeverse.brzodolokacije.data.models.User
import codeverse.brzodolokacije.data.repository.UserRepository
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class GetLikedUsersUseCase @Inject constructor(private val userRepository: UserRepository) {

    operator fun invoke(pageIndex: Int? = null, pageSize: Int? = null, searchTerm: String? = null) : Flow<Result<MutableList<User>>> = flow {
        try {

            emit(Result.Loading())

            val response = userRepository.getLikedUsers(pageIndex,pageSize,searchTerm)

            if (response.isSuccessful) {
                if (response.body() != null) {

                    emit(Result.Success(data = response.body()!!))
                }

            }
            else{
                emit(Result.Error("Došlo je do greške"))
            }
        } catch (e: HttpException) {
            emit(Result.Error(e.localizedMessage ?: Constants.UNEXPECTED_ERROR))
        } catch (e: IOException) {
            emit(Result.Error(Constants.SERVER_ERROR))
        }
    }
}