package codeverse.brzodolokacije.domain.use_case.comment

import codeverse.brzodolokacije.data.models.NewComment
import codeverse.brzodolokacije.data.models.UpdatedComment
import codeverse.brzodolokacije.data.repository.PostRepository
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UpdateCommentUseCase @Inject constructor(private val postRepository: PostRepository) {

    operator fun invoke(updatedComment: UpdatedComment) : Flow<Result<Unit>> = flow{
        try {
            emit(Result.Loading())
            val response = postRepository.updateComment(updatedComment)

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