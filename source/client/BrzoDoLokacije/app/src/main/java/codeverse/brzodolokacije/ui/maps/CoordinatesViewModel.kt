package codeverse.brzodolokacije.ui.maps

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import codeverse.brzodolokacije.data.models.UserCoordinateResponse
import codeverse.brzodolokacije.data.models.maps.LatLng
import codeverse.brzodolokacije.data.models.maps.coordinates.CoordinatesResponse
import codeverse.brzodolokacije.data.models.maps.input.Feature
import codeverse.brzodolokacije.domain.use_case.maps.GetCoordinatesByUserUseCase
import codeverse.brzodolokacije.domain.use_case.maps.SearchByCoordinatesUseCase
import codeverse.brzodolokacije.utils.MyState
import codeverse.brzodolokacije.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoordinatesViewModel @Inject constructor(private val searchByCoordinatesUseCase: SearchByCoordinatesUseCase,
                                               private val getCoordinatesByUserUseCase: GetCoordinatesByUserUseCase) : ViewModel() {

    private val _coordinatesState = mutableStateOf(MyState<CoordinatesResponse>())
    val coordinatesState: State<MyState<CoordinatesResponse>> = _coordinatesState

    private val _userCoordinatesState = mutableStateOf(MyState<ArrayList<UserCoordinateResponse>>())
    val userCoordinatesState: State<MyState<ArrayList<UserCoordinateResponse>>> = _userCoordinatesState


    fun onCoordinatesSearch(
        newLocation: MutableState<Feature?>,
        longitude: Double,
        latitude: Double,
        onConfirmMap: (feature: Feature) -> Unit,
        showErrorToast: () -> Unit,
        onFinish: (feature: Feature) -> Boolean
    ) {
        searchByCoordinatesUseCase(longitude,latitude).onEach { result ->
            when (result) {
                is Result.Success -> {
                    println("USPEH")
                    _coordinatesState.value = MyState(success = true, data = result.data)
                    if (result.data!!.features.size > 0){
                        newLocation.value = result.data!!.features[0]
                        onConfirmMap(newLocation.value!!)
                        onFinish(newLocation.value!!)
                    }
                    else{
                        //LOGIKA UKOLIKO JE IZABRAO KOORDINATE ZA LOKACIJU KOJU NE VRACA
                        println("Nema lokacije za ove koordinate")
                        showErrorToast()
                    }

                }
                is Result.Error -> {
                    println("DOSLO JE DO GRESKE")
                    _coordinatesState.value = MyState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }
                is Result.Loading -> {
                    _coordinatesState.value = MyState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getCoordinatesByUser(
        userId: Long,
        addMarkers: (ArrayList<UserCoordinateResponse>, (postId: Long) -> Unit) -> Unit,
        navigateToPost: (postId: Long) -> Unit
    ){

        getCoordinatesByUserUseCase(userId).onEach { result ->

            when (result) {
                is Result.Success -> {
                    _userCoordinatesState.value = MyState(success = true, data = result.data)
                    println("USPESNO DOVUCENE KOORDINATE KORISNIKA")
                    addMarkers(result.data!!, navigateToPost)

                }
                is Result.Error -> {
                    println("DOSLO JE DO GRESKE")
                    _userCoordinatesState.value = MyState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }
                is Result.Loading -> {
                    _userCoordinatesState.value = MyState(isLoading = true)
                }
            }

        }.launchIn(viewModelScope)
    }
}