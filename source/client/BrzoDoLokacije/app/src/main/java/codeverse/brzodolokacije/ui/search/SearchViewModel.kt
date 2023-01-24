package codeverse.brzodolokacije.ui.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import codeverse.brzodolokacije.data.models.PostItem
import codeverse.brzodolokacije.data.models.search.PostSortBy
import codeverse.brzodolokacije.data.models.search.SortOrder
import codeverse.brzodolokacije.domain.use_case.post.GetPostsBySearchUseCase
import codeverse.brzodolokacije.utils.MyState
import codeverse.brzodolokacije.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val getPostsBySearchUseCase: GetPostsBySearchUseCase): ViewModel() {

    var _placeName = mutableStateOf("")
    var _longitude = mutableStateOf<Double?>(null)
    var _latitude = mutableStateOf<Double?>(null)
    var _sortOrder: MutableState<SortOrder> = mutableStateOf(SortOrder.Descending)
    var _sortBy: MutableState<PostSortBy> = mutableStateOf(PostSortBy.CreatedDate)

    private val _stateSearchPosts = mutableStateOf(MyState<MutableList<PostItem>>())
    val stateSearchPosts : State<MyState<MutableList<PostItem>>> = _stateSearchPosts

    fun search(searchPosts: (state: MyState<MutableList<PostItem>>) -> Unit) {

        getPostsBySearchUseCase(_placeName.value,_longitude.value,_latitude.value,null,_sortBy.value.ordinal,_sortOrder.value.ordinal).onEach{ result ->
            when (result) {
                is Result.Success -> {
                    _stateSearchPosts.value = MyState(success = true, data = result.data)
                    println("USPESNO!")
                }
                is Result.Error -> {
                    _stateSearchPosts.value = MyState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    println("GRESKA")
                }
                is Result.Loading -> {
                    _stateSearchPosts.value = MyState(isLoading = true)
                }
            }
            searchPosts(_stateSearchPosts.value)
        }.launchIn(viewModelScope)
    }

    fun getCoordinates(){

    }
}