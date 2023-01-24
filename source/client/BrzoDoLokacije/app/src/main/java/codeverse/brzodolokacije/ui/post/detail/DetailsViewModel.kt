package codeverse.brzodolokacije.ui.post.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import codeverse.brzodolokacije.data.models.NewComment
import codeverse.brzodolokacije.data.models.PostItem
import codeverse.brzodolokacije.data.models.UpdatedComment
import codeverse.brzodolokacije.data.models.RatePost
import codeverse.brzodolokacije.data.models.User
import codeverse.brzodolokacije.domain.use_case.comment.AddCommentUseCase
import codeverse.brzodolokacije.domain.use_case.comment.DeleteCommentUseCase
import codeverse.brzodolokacije.domain.use_case.comment.UpdateCommentUseCase
import codeverse.brzodolokacije.domain.use_case.post.DeletePostUseCase
import codeverse.brzodolokacije.domain.use_case.post.GetPostByIdUseCase
import codeverse.brzodolokacije.domain.use_case.rate.AddRateUseCase
import codeverse.brzodolokacije.domain.use_case.rate.DeleteRateUseCase
import codeverse.brzodolokacije.domain.use_case.user.LikeUserUseCase
import codeverse.brzodolokacije.utils.*
import codeverse.brzodolokacije.utils.managers.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val userManager: UserManager,
                                           savedStateHandle: SavedStateHandle,
                                           private val getPostByIdUseCase: GetPostByIdUseCase,
                                           private val deletePostUseCase: DeletePostUseCase,
                                           private val addRateUseCase: AddRateUseCase,
                                           private val deleteRateUseCase: DeleteRateUseCase,
                                           private val likeUserUseCase: LikeUserUseCase): ViewModel() {


    var post = mutableStateOf<PostItem?>(null);
    val me = userManager.getUser()

    private val _postState = mutableStateOf(MyState<PostItem>())
    val postState: State<MyState<PostItem>> = _postState

    private val _deletedState = mutableStateOf(MyState<Unit>())
    val deletedState: State<MyState<Unit>> = _deletedState

    private val _rateState = mutableStateOf(MyState<Double>())
    val rateState: State<MyState<Double>> = _rateState

    var _deleteDialogOpen = mutableStateOf(false)
    var _isRated = mutableStateOf(false)
    var _myRate = mutableStateOf<Int>(0)
    var _avgRating = mutableStateOf<Double>(0.0)

    init {
        savedStateHandle.get<Int>("postDetailId")?.let { postId ->

            this.getPostById(postId)
        }
    }

    fun isMe(): Boolean{
        if(post.value == null || me == null) return false
        return post.value!!.createdBy.id == me.id
    }

    fun getPostById(postId: Int){
        getPostByIdUseCase(postId.toLong()).onEach { result ->
            when(result){
                is Result.Success ->{
                    _postState.value = MyState(success = true, data = result.data)
                    post.value = result.data
                    _avgRating.value = result.data!!.avgRating

                    if (post.value!!.userRate != null){
                        _isRated.value = true
                        _myRate.value = post.value!!.userRate!!
                    }
                    else{
                        _isRated.value = false
                    }
                    println("USPESNO DOVUCEN POST")
                }
                is Result.Error ->{
                    _postState.value = MyState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    println("GRESKA")
                }
                is Result.Loading ->{
                    _postState.value = MyState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun deletePost(postId: Long){
        deletePostUseCase(postId).onEach { result ->
            when(result){
                is Result.Success -> {
                    _deletedState.value = MyState(success = true, data = result.data)
                    println("USPESNO IZBRISAN POST!")
                }
                is Result.Error -> {
                    _deletedState.value = MyState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    println("GRESKA")
                }
                is Result.Loading -> {
                    _deletedState.value = MyState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun addRate(rate: Int){

        val newRate : RatePost = RatePost(post.value!!.id, rate)
        addRateUseCase(newRate).onEach { result ->
            when(result){
                is Result.Success -> {
                    _rateState.value = MyState(success = true, data = result.data)
                    println("USPESNO DODATA OCENA!")
                    _isRated.value = true
                    _myRate.value = rate
                    _avgRating.value = result.data!!
                }
                is Result.Error -> {
                    _rateState.value = MyState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    println("GRESKA")
                }
                is Result.Loading -> {
                    _rateState.value = MyState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun deleteRate(){

        deleteRateUseCase(post.value!!.id).onEach { result ->
            when(result){
                is Result.Success -> {
                    _rateState.value = MyState(success = true, data = result.data)
                    println("USPESNO IZBRISANA OCENA!")
                    _isRated.value = false
                    _myRate.value = 0
                    _avgRating.value = result.data!!
                }
                is Result.Error -> {
                    _rateState.value = MyState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    println("GRESKA")
                }
                is Result.Loading -> {
                    _rateState.value = MyState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun performRate(rate: Int){

        if (_isRated.value){
            if (rate != _myRate.value) addRate(rate) //za sada impelemntirano da se ocena brise kada pritisne jos jednom na istu ocenu
            else deleteRate()
        }
        else{
            addRate(rate)
        }

    }

    fun onDissmisDialog(){
        _deleteDialogOpen.value = false
    }

    fun onConfirmDialog(){
        _deleteDialogOpen.value = false

        deletePost(post.value!!.id)
    }
}