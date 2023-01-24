package codeverse.brzodolokacije.domain.use_case.post

import codeverse.brzodolokacije.data.models.CustomResponse
import codeverse.brzodolokacije.data.repository.PostRepository
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import javax.inject.Inject

class PostImagesUseCase @Inject constructor(private val postRepository: PostRepository) {

    operator fun invoke(images: List<File>): Flow<Result<CustomResponse<Long?>>> = flow {
        try {
            emit(Result.Loading())
            val imageResponse = postRepository.postImages(images)

            if (imageResponse.isSuccessful) {
                if (imageResponse.body() != null) {
                    emit(Result.Success(imageResponse.body()!!))
                    println("Stigao je odgovor za slike")
                }
            }
            else{
                println(imageResponse.code())
                println(imageResponse.errorBody())
            }
        } catch (e: HttpException) {
            emit(Result.Error(e.localizedMessage ?: Constants.UNEXPECTED_ERROR))
        } catch (e: IOException) {
            emit(Result.Error(Constants.SERVER_ERROR))
        }
    }

}