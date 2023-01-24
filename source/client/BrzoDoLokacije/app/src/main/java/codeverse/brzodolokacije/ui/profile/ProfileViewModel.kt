package codeverse.brzodolokacije.ui.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import codeverse.brzodolokacije.data.models.PostItem
import codeverse.brzodolokacije.data.models.User
import codeverse.brzodolokacije.data.models.UsersPostResponse
import codeverse.brzodolokacije.data.models.maps.input.Feature
import codeverse.brzodolokacije.data.models.search.PostSortBy
import codeverse.brzodolokacije.data.models.search.SortOrder
import codeverse.brzodolokacije.data.paginator.DefaultPaginator
import codeverse.brzodolokacije.data.paginator.ScreenState
import codeverse.brzodolokacije.data.repository.PostRepository
import codeverse.brzodolokacije.domain.use_case.post.GetPostsByUserUseCase
import codeverse.brzodolokacije.domain.use_case.user.GetUserByIdUseCase
import codeverse.brzodolokacije.domain.use_case.user.LikeUserUseCase
import codeverse.brzodolokacije.utils.*
import codeverse.brzodolokacije.utils.managers.TokenManager
import codeverse.brzodolokacije.utils.managers.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userManager: UserManager,
                                           private val tokenManager: TokenManager,
                                           private val getUserByIdUseCase: GetUserByIdUseCase,
                                           private val postRepository: PostRepository,
                                           savedStateHandle: SavedStateHandle,
                                           private val getPostsByUserUseCase: GetPostsByUserUseCase,
                                           private val likeUserUseCase: LikeUserUseCase) : ViewModel() {

    var _user : User? = null
    var userId: Long = 0

    private val _postsState = mutableStateOf(MyState<UsersPostResponse>())
    val postsState: State<MyState<UsersPostResponse>> = _postsState

    private val _userState = mutableStateOf(MyState<User>())
    val userState: State<MyState<User>> = _userState

    private val _stateLikeUser = mutableStateOf(MyState<Unit>())
    val stateLikeUser : State<MyState<Unit>> = _stateLikeUser

    var _posts = mutableStateOf(mutableListOf<PostItem>());
    var _isMe  = mutableStateOf(false)
    var _isLikedIndicator = mutableStateOf(false)
    var _openDialogLogout = mutableStateOf(false)


    var paginationState by mutableStateOf(ScreenState<PostItem>())

    private val paginator = DefaultPaginator(
        initialKey =  paginationState.pageIndex,
        onLoadUpdated = {
            paginationState = paginationState.copy(isLoading = it)
        },
        onRequest = { nextPageIndex ->
            postRepository.getPostsByUser(userId, pageIndex = nextPageIndex, pageSize = Constants.PAGE_SIZE)
        },
        getNextKey = {
            paginationState.pageIndex + Constants.PAGE_SIZE
        },
        onError = {
            paginationState = paginationState.copy(error = it)
        },
        onSuccess = { response, newKey ->
            _postsState.value = MyState(success = true, data = response)
            paginationState = paginationState.copy(
                items = (paginationState.items + response.posts) as MutableList<PostItem>,
                pageIndex = newKey,
                endReached = response.posts.isEmpty()
            )

        }
    )

    init {
        savedStateHandle.get<String>("profileId")?.let { userId ->
            this.userId = userId.toLong()
        }
        val user = userManager.getUser()
        if (user!= null) {
            if (userId == 0L || userId == user.id){
                userId = user.id
                _isMe.value = true
            }
        }

        getUserById(this.userId)
    }

    fun logout(){
        _openDialogLogout.value = false
        userManager.deleteUser()
        tokenManager.deleteToken()

    }

    fun loadItems(){
        println("Trenutno ima " + paginationState.items.size + " postova")
        println("Sada ucitavam jos...")
        paginator.loadNextItems().launchIn(viewModelScope)
    }

    fun getUserById(userId: Long){
        getUserByIdUseCase(userId).onEach { result ->
            when (result) {
                is Result.Success -> {
                    _userState.value = MyState(success = true, data = result.data)
                    _user = result.data
                    _isLikedIndicator.value = _user!!.isLikedByCurrentUser
                    println("Uspesno dovlacenje korisnika!")
                    loadItems()
                    //getPostsByUser(userId)
                }
                is Result.Error -> {
                    _userState.value = MyState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    println("GRESKA")
                }
                is Result.Loading -> {
                    _userState.value = MyState(isLoading = true)
                }
            }

        }.launchIn(viewModelScope)
    }
    fun getPostsByUser(id: Long){
        getPostsByUserUseCase(id).onEach { result ->
            when(result){
                is Result.Success -> {
                    _postsState.value = MyState(success = true, data = result.data)
                    _posts.value = result.data!!.posts;
                    println("USPESNO DOVUCENI POSTOVI!")
                }
                is Result.Error -> {
                    _postsState.value = MyState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    println("GRESKA")
                }
                is Result.Loading -> {
                    _postsState.value = MyState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun likeUser(like:Boolean){
        likeUserUseCase(_user!!.id, like).onEach { result ->
            when (result) {
                is Result.Success -> {
                    _stateLikeUser.value = MyState(success = true, data = result.data)
                    _isLikedIndicator.value = like
                    println("Uspesno lajkovanje/dislajkovanje!")
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

}