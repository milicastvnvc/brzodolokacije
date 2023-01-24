package codeverse.brzodolokacije.domain.use_case.post

import codeverse.brzodolokacije.data.models.PostItem
import codeverse.brzodolokacije.data.repository.PostRepository
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetPostByIdUseCase @Inject constructor(private val postRepository: PostRepository) {

    operator fun invoke(postId: Long) : Flow<Result<PostItem>> = flow{
        println("Je l udjes ti ovde?")
        try {
            emit(Result.Loading())
            val response = postRepository.getPostById(postId)

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
                println(response.code())
                println(response.errorBody())
                emit(Result.Error("Došlo je do greške."))
            }
        } catch (e: HttpException) {
            emit(Result.Error(e.localizedMessage ?: Constants.UNEXPECTED_ERROR))
        } catch (e: IOException) {
            emit(Result.Error(Constants.SERVER_ERROR))
        }

    }
}