package codeverse.brzodolokacije.domain.use_case.maps

import codeverse.brzodolokacije.data.models.maps.coordinates.CoordinatesResponse
import codeverse.brzodolokacije.data.models.maps.input.InputResponse
import codeverse.brzodolokacije.data.repository.MapsRepository
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SearchByInputUseCase @Inject constructor(
    private val mapsRepository: MapsRepository
) {

    operator fun invoke(place: String): Flow<Result<InputResponse>> = flow {
        try {
            emit(Result.Loading())
            val response = mapsRepository.searchByInput(place)

            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            }
            else{
                emit(Result.Error("Došlo je do greške"))
                println(response.code())
                println(response.errorBody())
            }
        } catch (e: HttpException) {
            emit(Result.Error(e.localizedMessage ?: Constants.UNEXPECTED_ERROR))
        } catch (e: IOException) {
            emit(Result.Error(Constants.SERVER_ERROR))
        }
    }
}