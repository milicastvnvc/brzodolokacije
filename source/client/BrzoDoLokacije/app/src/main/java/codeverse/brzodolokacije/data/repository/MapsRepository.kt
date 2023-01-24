package codeverse.brzodolokacije.data.repository

import codeverse.brzodolokacije.data.apis.MapsApi
import codeverse.brzodolokacije.data.models.maps.coordinates.CoordinatesResponse
import codeverse.brzodolokacije.data.models.maps.input.InputResponse
import retrofit2.Response
import javax.inject.Inject

class MapsRepository @Inject constructor(
    private val mapsApi : MapsApi) {


    suspend fun searchByInput(place: String): Response<InputResponse> {
        return mapsApi.searchByInput(place)
    }
    suspend fun searchByCoordinates(longitude: Double, latitude: Double): Response<CoordinatesResponse> {
        return mapsApi.searchByCoordinates(longitude,latitude)
    }
}