package codeverse.brzodolokacije.ui.home

import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import codeverse.brzodolokacije.data.models.PostItem
import codeverse.brzodolokacije.data.models.search.PostSortBy
import codeverse.brzodolokacije.data.models.search.SortOrder
import codeverse.brzodolokacije.data.paginator.DefaultPaginator
import codeverse.brzodolokacije.data.paginator.ScreenState
import codeverse.brzodolokacije.data.repository.PostRepository
import codeverse.brzodolokacije.domain.use_case.post.GetPostsBySearchUseCase
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.MyState
import codeverse.brzodolokacije.utils.managers.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getPostsBySearchUseCase: GetPostsBySearchUseCase,
                                        private val postRepository: PostRepository,
                                        savedStateHandle: SavedStateHandle) : ViewModel() {

    @Inject
    lateinit var tokenManager: TokenManager

    var _placeName = mutableStateOf("")
    var _longitude = mutableStateOf<Double?>(null)
    var _latitude = mutableStateOf<Double?>(null)
    var _tags = mutableStateOf<MutableList<String>>(mutableListOf())
    var _sortOrder: MutableState<SortOrder> = mutableStateOf(SortOrder.Descending)
    var _sortBy: MutableState<PostSortBy> = mutableStateOf(PostSortBy.CreatedDate)

    var paginationState by mutableStateOf(ScreenState<PostItem>())

    private val paginator = DefaultPaginator(
        initialKey =  paginationState.pageIndex,
        onLoadUpdated = {
            paginationState = paginationState.copy(isLoading = it)
        },
        onRequest = { nextPageIndex ->
            postRepository.getPostsBySearch(_placeName.value,
                _longitude.value,_latitude.value,
                _tags.value,_sortBy.value.ordinal,_sortOrder.value.ordinal,
                pageIndex = nextPageIndex, pageSize = Constants.PAGE_SIZE)
        },
        getNextKey = {
            paginationState.pageIndex + Constants.PAGE_SIZE
        },
        onError = {
            paginationState = paginationState.copy(error = it)
        },
        onSuccess = { items, newKey ->
            paginationState = paginationState.copy(
            items = (paginationState.items + items) as MutableList<PostItem>,
            pageIndex = newKey,
            endReached = items.isEmpty()
            )

        }
    )

    private val _stateSearchPosts = mutableStateOf(MyState<MutableList<PostItem>>())
    val stateSearchPosts : State<MyState<MutableList<PostItem>>> = _stateSearchPosts

    private val _posts = mutableStateOf<MutableList<PostItem>>(mutableListOf())
    val posts: State<MutableList<PostItem>> = _posts

    var _tagDialogOpen = mutableStateOf(false)
    var _mapOpen = mutableStateOf(false)
    var isRefreshing = mutableStateOf(false)

    init{
        savedStateHandle.get<String>("tag")?.let { tag ->
            _tags.value!!.add(tag)
        }
        savedStateHandle.get<String>("searchLatitude")?.let { latitude ->
            _latitude.value = latitude.toDouble()
        }
        savedStateHandle.get<String>("searchLongitude")?.let { longitude ->
            _longitude.value = longitude.toDouble()
        }
        savedStateHandle.get<String>("placeName")?.let { placeName ->
            _placeName.value = placeName
        }
        //search()
        loadItems()
    }

    fun refresh(){
        _placeName.value = ""
        _longitude.value = null
        _latitude.value = null
        _tags.value = mutableListOf()
        _sortOrder.value = SortOrder.Descending
        _sortBy.value = PostSortBy.CreatedDate

        resetPaginationState()
        loadItems()
        //search()
    }
    fun deleteToken() { //test
        println(tokenManager.getToken())

        tokenManager.deleteToken()
    }

    fun search() {

        resetPaginationState()
        loadItems()
//        getPostsBySearchUseCase(_placeName.value,_longitude.value,_latitude.value,_tags.value,_sortBy.value.ordinal,_sortOrder.value.ordinal).onEach{ result ->
//            when (result) {
//                is Result.Success -> {
//                    _stateSearchPosts.value = MyState(success = true, data = result.data)
//                    _posts.value = result.data!!
//                    println("USPESNO!")
//                }
//                is Result.Error -> {
//                    _stateSearchPosts.value = MyState(
//                        error = result.message ?: "An unexpected error occured"
//                    )
//                    println("GRESKA")
//                }
//                is Result.Loading -> {
//                    _stateSearchPosts.value = MyState(isLoading = true)
//                }
//            }
//        }.launchIn(viewModelScope)
    }

    fun loadItems(){
        println("Trenutno ima " + paginationState.items.size + " postova")
        println("Sada ucitavam jos...")
        paginator.loadNextItems().launchIn(viewModelScope)
    }

    fun resetPaginationState(){
        paginationState.isLoading = false
        paginationState.items = mutableListOf()
        paginationState.error = null
        paginationState.endReached= false
        paginationState.pageIndex = 0
        paginator.reset()
    }

    fun onDismissTagDialog(){
        _tagDialogOpen.value = false;
    }

    fun onConfirmTagDialog(tag: String){
        _tags.value.add(tag.trim())
        _tagDialogOpen.value = false;
        search()
    }

    fun deleteTag(tag: String){
        _tags.value = _tags.value.filter { it != tag }?.toMutableList()
        search()
    }
    fun deleteCoordinates(string: String){
        _latitude.value = null
        _longitude.value = null
        search()
    }
}