package codeverse.brzodolokacije.data.apis

import codeverse.brzodolokacije.data.models.maps.coordinates.CoordinatesResponse
import codeverse.brzodolokacije.data.models.maps.input.InputResponse
import codeverse.brzodolokacije.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MapsApi {

    @GET("{place}.json?key="+Constants.MAPTILER_API_KEY+"&language=en,sr")
    suspend fun searchByInput(@Path("place") place: String) : Response<InputResponse>

    @GET("{longitude},{latitude}.json?key="+Constants.MAPTILER_API_KEY+"&language=en,sr")
    suspend fun searchByCoordinates(@Path("longitude") longitude: Double, @Path("latitude") latitude: Double) : Response<CoordinatesResponse>
}