package codeverse.brzodolokacije.domain.use_case.maps

import codeverse.brzodolokacije.data.models.maps.coordinates.CoordinatesResponse
import codeverse.brzodolokacije.data.repository.MapsRepository
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SearchByCoordinatesUseCase @Inject constructor(
    private val mapsRepository: MapsRepository) {

    operator fun invoke(longitude: Double, latitude: Double): Flow<Result<CoordinatesResponse>> = flow {
        try {
            emit(Result.Loading())
            val response = mapsRepository.searchByCoordinates(longitude,latitude)

            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            }
            else{
                emit(Result.Error("Došlo je do greške"))
                println(response.code())
                println(response.errorBody())
            }
        } catch (e: HttpException) {
            e.printStackTrace()
            emit(Result.Error(e.localizedMessage ?: Constants.UNEXPECTED_ERROR))
        } catch (e: IOException) {
            e.printStackTrace()
            emit(Result.Error(Constants.SERVER_ERROR))
        }
    }
}