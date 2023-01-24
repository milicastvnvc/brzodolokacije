package codeverse.brzodolokacije.domain.use_case.post

import codeverse.brzodolokacije.data.models.UsersPostResponse
import codeverse.brzodolokacije.data.repository.PostRepository
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetPostsByUserUseCase @Inject constructor(private val postRepository: PostRepository) {

    operator fun invoke(userId: Long, pageIndex: Int = 0, pageSize: Int = 20) : Flow<Result<UsersPostResponse>> = flow{
        try {
            emit(Result.Loading())
            val userPostsResponse = postRepository.getPostsByUser(userId,pageIndex,pageSize)

            if (userPostsResponse.isSuccessful) {
                if (userPostsResponse.body() != null) {
                    emit(Result.Success(userPostsResponse.body()!!))
                }
            }
            else{
                println(userPostsResponse.code())
                println(userPostsResponse.errorBody())
                emit(Result.Error("Došlo je do greške"))
            }
        } catch (e: HttpException) {
            emit(Result.Error<UsersPostResponse>(e.localizedMessage ?: Constants.UNEXPECTED_ERROR))
        } catch (e: IOException) {
            emit(Result.Error<UsersPostResponse>(Constants.SERVER_ERROR))
        }

    }
}