package codeverse.brzodolokacije.ui.maps.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import codeverse.brzodolokacije.data.models.maps.input.InputResponse
import codeverse.brzodolokacije.domain.use_case.maps.SearchByCoordinatesUseCase
import codeverse.brzodolokacije.domain.use_case.maps.SearchByInputUseCase
import codeverse.brzodolokacije.utils.MyState
import codeverse.brzodolokacije.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SearchMapViewModel @Inject constructor(private val searchByCoordinatesUseCase: SearchByCoordinatesUseCase,
                                             private val searchByInputUseCase: SearchByInputUseCase
) : ViewModel(){

    private val _inputPlaceState = mutableStateOf(MyState<InputResponse>())
    val inputPlaceState: State<MyState<InputResponse>> = _inputPlaceState

    fun onInputSearch(text: String){
        searchByInputUseCase(text).onEach { result ->
            when (result) {
                is Result.Success -> {
                    println("USPEH")
                    _inputPlaceState.value = MyState(success = true, data = result.data)

                }
                is Result.Error -> {
                    _inputPlaceState.value = MyState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }
                is Result.Loading -> {
                    _inputPlaceState.value = MyState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}