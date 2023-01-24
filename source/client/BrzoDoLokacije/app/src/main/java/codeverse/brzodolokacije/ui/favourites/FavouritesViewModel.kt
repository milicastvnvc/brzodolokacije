package codeverse.brzodolokacije.ui.favourites

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import codeverse.brzodolokacije.data.models.PostItem
import codeverse.brzodolokacije.data.models.User
import codeverse.brzodolokacije.data.models.search.PostSortBy
import codeverse.brzodolokacije.data.models.search.SortOrder
import codeverse.brzodolokacije.data.paginator.DefaultPaginator
import codeverse.brzodolokacije.data.paginator.ScreenState
import codeverse.brzodolokacije.data.repository.UserRepository
import codeverse.brzodolokacije.domain.use_case.user.GetLikedUsersUseCase
import codeverse.brzodolokacije.domain.use_case.user.LikeUserUseCase
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.MyState
import codeverse.brzodolokacije.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(private val getLikedUsersUseCase: GetLikedUsersUseCase,
                                              private val likeUserUseCase: LikeUserUseCase,
                                              private val userRepository: UserRepository
): ViewModel() {


    private val _stateFavourites = mutableStateOf(MyState<List<User>>())
    val stateFavourites : State<MyState<List<User>>> = _stateFavourites

    private val _stateLikeUser = mutableStateOf(MyState<Unit>())
    val stateLikeUser : State<MyState<Unit>> = _stateLikeUser

    private val _favourites = mutableStateOf<List<User>>(mutableListOf())
    val favourites: State<List<User>> = _favourites

    var searchTerm  = mutableStateOf<String>("")
    var paginationState by mutableStateOf(ScreenState<User>())

    var isRefreshing = mutableStateOf(false)
    var _dislikeDialogOpen = mutableStateOf(false)
    var _userToDislike = mutableStateOf<User?>(null)

    fun refresh(){
        _stateLikeUser.value = MyState<Unit>()
        searchTerm.value = ""

        searchFavourites()
        //search()
    }

    private val paginator = DefaultPaginator(
        initialKey =  paginationState.pageIndex,
        onLoadUpdated = {
            paginationState = paginationState.copy(isLoading = it)
        },
        onRequest = { nextPageIndex ->
            userRepository.getLikedUsers(pageIndex = nextPageIndex,
                pageSize = Constants.PAGE_SIZE,
                searchTerm = searchTerm.value)
        },
        getNextKey = {
            paginationState.pageIndex + Constants.PAGE_SIZE
        },
        onError = {
            paginationState = paginationState.copy(error = it)
        },
        onSuccess = { users, newKey ->
            _stateFavourites.value = MyState(success = true, data = users)
            paginationState = paginationState.copy(
                items = (paginationState.items + users) as MutableList<User>,
                pageIndex = newKey,
                endReached = users.isEmpty()
            )

        }
    )


    init{
        loadItems()
    }

    fun loadItems(){
        println("Trenutno ima " + paginationState.items.size + " postova")
        println("Sada ucitavam jos...")
        paginator.loadNextItems().launchIn(viewModelScope)
    }

    fun searchFavourites(){
        resetPaginationState()
        loadItems()
    }

    fun resetPaginationState(){
        paginationState.isLoading = false
        paginationState.items = mutableListOf()
        paginationState.error = null
        paginationState.endReached= false
        paginationState.pageIndex = 0
        paginator.reset()
    }


    fun getLikedUsers(searchTerm: String? = null,pageIndex: Int? = null, pageSize: Int? = null){

        getLikedUsersUseCase(pageIndex,pageSize,searchTerm).onEach{ result ->
            when (result) {
                is Result.Success -> {
                    _stateFavourites.value = MyState(success = true, data = result.data)
                    _favourites.value = result.data!!
                    println("Uspesno dovlacenje lajkovanih korisnika!")
                }
                is Result.Error -> {
                    _stateFavourites.value = MyState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    println("GRESKA")
                }
                is Result.Loading -> {
                    _stateFavourites.value = MyState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun likeUser(userId: Long, like:Boolean){
        likeUserUseCase(userId, like).onEach { result ->
            when (result) {
                is Result.Success -> {
                    _stateLikeUser.value = MyState(success = true, data = result.data)

                    println("Uspesno lajkovanje/dislajkovanje!")
                    searchFavourites()
                }
                is Result.Error -> {
                    _stateLikeUser.value = MyState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    println("GRESKA")
                }
                is Result.Loading -> {
                    _stateLikeUser.value = MyState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onDissmisDialog(){
        _dislikeDialogOpen.value = false
    }

    fun onConfirmDialog(){
        if (_userToDislike.value != null){
            println("Dislajkujem usera sa usernameom " + _userToDislike.value!!.username)
            likeUser(_userToDislike.value!!.id, false)
        }
        _dislikeDialogOpen.value = false
    }
}