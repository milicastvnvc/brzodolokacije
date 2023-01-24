package codeverse.brzodolokacije.ui.comment

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import codeverse.brzodolokacije.data.models.Comment
import codeverse.brzodolokacije.data.models.NewComment
import codeverse.brzodolokacije.data.models.PostItem
import codeverse.brzodolokacije.data.models.UpdatedComment
import codeverse.brzodolokacije.domain.use_case.comment.AddCommentUseCase
import codeverse.brzodolokacije.domain.use_case.comment.DeleteCommentUseCase
import codeverse.brzodolokacije.domain.use_case.comment.UpdateCommentUseCase
import codeverse.brzodolokacije.domain.use_case.post.GetPostByIdUseCase
import codeverse.brzodolokacije.utils.MyState
import codeverse.brzodolokacije.utils.Result
import codeverse.brzodolokacije.utils.managers.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(private val getPostByIdUseCase: GetPostByIdUseCase,
                                           private val addCommentUseCase: AddCommentUseCase,
                                           private val deleteCommentUseCase: DeleteCommentUseCase,
                                           private val updateCommentUseCase: UpdateCommentUseCase,
                                           savedStateHandle: SavedStateHandle,
                                           private val userManager: UserManager) : ViewModel() {

    private val _postState = mutableStateOf(MyState<PostItem>())
    val postState: State<MyState<PostItem>> = _postState

    private val _addCommentState = mutableStateOf(MyState<Unit>())
    val addCommentState : State<MyState<Unit>> = _addCommentState

    private val _deleteCommentState = mutableStateOf(MyState<Unit>())
    val deleteCommentState : State<MyState<Unit>> = _deleteCommentState

    private val _updateCommentState = mutableStateOf(MyState<Unit>())
    val updateCommentState : State<MyState<Unit>> = _updateCommentState

    var comments = mutableStateOf<MutableList<Comment>?>(null)
    var answerTo = mutableStateOf<Comment?>(null)
    var tempCommentToUpdate = mutableStateOf<Comment?>(null)
    var post = mutableStateOf<PostItem?>(null)
    var postId = 0

    var _updateComment = mutableStateOf(mutableListOf<String>())
    var _updateCommentDialog = mutableStateOf(false)

    val me = userManager.getUser()

    init{
        savedStateHandle.get<Int>("commentDetailId")?.let { postId ->
            this.postId = postId
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
                    post.value = result.data!!
                    comments.value = result.data!!.comments as MutableList<Comment>
                    for (comment in comments.value!!){
                        println("Komentar sa id-jem " + comment.id + " je: ")
                        println(comment.text)
                        if(comment.parentCommentId != null){
                            println("ima roditelja i to je komentar sa id-jem " + comment.parentCommentId)
                        }
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

    fun addComment(postId: Long, text: String, parentCommentId: Long? = null){
        val newComment = NewComment(postId, text, parentCommentId)

        addCommentUseCase(newComment).onEach { result ->
            when(result){
                is Result.Success ->{
                    _addCommentState.value = MyState(success = true, data = result.data)
                    println("USPESNO DODAT KOMENTAR")
                    getPostById(this.postId)
                }
                is Result.Error ->{
                    _addCommentState.value = MyState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    println("GRESKA")
                }
                is Result.Loading ->{
                    _addCommentState.value = MyState(isLoading = true)
                }
            }

        }.launchIn(viewModelScope)
    }

    fun deleteComment(commentId: Long){

        deleteCommentUseCase(commentId).onEach { result ->
            when(result){
                is Result.Success ->{
                    _deleteCommentState.value = MyState(success = true, data = result.data)
                    getPostById(this.postId)
                    println("USPESNO IZBRISAN KOMENTAR")
                }
                is Result.Error ->{
                    _deleteCommentState.value = MyState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    println("GRESKA")
                }
                is Result.Loading ->{
                    _deleteCommentState.value = MyState(isLoading = true)
                }
            }

        }.launchIn(viewModelScope)
    }

    fun updateComment(commentId: Long, text: String){
        val updatedComment = UpdatedComment(commentId, text)

        updateCommentUseCase(updatedComment).onEach { result ->
            when(result){
                is Result.Success ->{
                    _updateCommentState.value = MyState(success = true, data = result.data)
                    println("USPESNO IZMENJEN KOMENTAR")
                    getPostById(this.postId)
                }
                is Result.Error ->{
                    _updateCommentState.value = MyState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    println("GRESKA")
                }
                is Result.Loading ->{
                    _updateCommentState.value = MyState(isLoading = true)
                }
            }

        }.launchIn(viewModelScope)
    }
    fun onDismissUpdateCommentDialog(){
        _updateCommentDialog.value = false;
    }

    fun onConfirmUpdateCommentDialog(commentId: Long, text: String){

        _updateComment.value.add(text.trim())
        updateComment(commentId, text.trim())
        _updateCommentDialog.value = false
    }
}