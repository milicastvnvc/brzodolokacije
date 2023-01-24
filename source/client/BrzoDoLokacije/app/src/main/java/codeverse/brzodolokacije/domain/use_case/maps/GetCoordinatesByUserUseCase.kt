package codeverse.brzodolokacije.domain.use_case.maps

import codeverse.brzodolokacije.data.models.NewPost
import codeverse.brzodolokacije.data.models.UserCoordinateResponse
import codeverse.brzodolokacije.data.models.maps.LatLng
import codeverse.brzodolokacije.data.repository.PostRepository
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCoordinatesByUserUseCase @Inject constructor(private val postRepository: PostRepository) {

    operator fun invoke(userId: Long): Flow<Result<ArrayList<UserCoordinateResponse>>> = flow {
        try {
            emit(Result.Loading())
            val response = postRepository.getCoordinatesByUser(userId)

            if (response.isSuccessful) {
                if (response.body() != null) {
                    emit(Result.Success(response.body()!!))

                }
            }
            else{
                println(response.code())
                println(response.message())
                emit(Result.Error("Došlo je do greške."))
            }
        } catch (e: HttpException) {
            emit(Result.Error(e.localizedMessage ?: Constants.UNEXPECTED_ERROR))
        } catch (e: IOException) {
            emit(Result.Error(Constants.SERVER_ERROR))
        }
    }
}