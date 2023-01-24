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

class UploadImagesUseCase @Inject constructor(private val postRepository: PostRepository) {

    operator fun invoke(postId: Long, images: MutableList<File>): Flow<Result<CustomResponse<Unit>>> = flow {
        try {
            emit(Result.Loading())
            val imageResponse = postRepository.uploadImages(postId, images)

            if (imageResponse.isSuccessful) {
                if (imageResponse.body() != null) {
                    if (imageResponse.body()!!.success){
                        emit(Result.Success(imageResponse.body()!!))
                        println("Stigao je uspesan odgovor za slike!!!")
                    }
                    else if (imageResponse.body()!!.errors.size > 0){
                        emit(Result.Error(imageResponse.body()!!.errors[0]))
                    }

                }
            }
            else{
                println(imageResponse.code())
                println(imageResponse.errorBody())
                emit(Result.Error("Došlo je do greške!"))
            }
        } catch (e: HttpException) {
            println(e.message())
            emit(Result.Error(e.localizedMessage ?: Constants.UNEXPECTED_ERROR))
        } catch (e: IOException) {
            println(e.message)
            emit(Result.Error(Constants.SERVER_ERROR))
        }
    }

}