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

class GetPostsBySearchUseCase @Inject constructor(private val postRepository: PostRepository) {

    operator fun invoke(placeName: String, longitude: Double? = null,
                        latitude: Double? = null, tags: MutableList<String>? = null,
                        sortBy: Int = 0, sortOrder: Int = 1,
                        pageIndex: Int = 0, pageSize: Int = 20): Flow<Result<MutableList<PostItem>>> = flow {
        try {
            emit(Result.Loading())
            val response = postRepository.getPostsBySearch(placeName,longitude,latitude,tags,sortBy,sortOrder,pageIndex,pageSize)

            if (response.isSuccessful) {

                if (response.body() != null) {
                    emit(Result.Success(response.body()!!))
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