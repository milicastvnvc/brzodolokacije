package codeverse.brzodolokacije.ui.profile.edit

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import codeverse.brzodolokacije.data.models.UpdatedUser
import codeverse.brzodolokacije.data.models.User
import codeverse.brzodolokacije.domain.use_case.user.EditUserUseCase
import codeverse.brzodolokacije.utils.helpers.ImageHelper
import codeverse.brzodolokacije.utils.MyState
import codeverse.brzodolokacije.utils.Result
import codeverse.brzodolokacije.utils.managers.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val editUserUseCase: EditUserUseCase,
                                               private val userManager: UserManager
) : ViewModel() {

    val current_user = userManager.getUser()!!

    private val _editState = mutableStateOf(MyState<User>())
    val editState: State<MyState<User>> = _editState

    var _editSuccess = mutableStateOf(false)
    var _imageAlert = mutableStateOf("")


    fun editUser(firstName: String, lastName: String, username: String, image:Bitmap? = null){

        var updatedUser: UpdatedUser;
        if(image != null){
            val base64 = ImageHelper.fromBitmapToBase64(image)
            updatedUser = UpdatedUser(current_user.id,username,firstName,lastName, true, base64)
        }
        else{
            updatedUser = UpdatedUser(current_user.id,username,firstName,lastName)
        }

        editUserUseCase(updatedUser).onEach { result ->

            when (result) {
                is Result.Success -> {
                    println("USPEH")
                    _editState.value = MyState(success = true, data = result.data)
                    userManager.saveUser(result.data!!)
                    if(!_editSuccess.value)
                        _editSuccess.value = true;
                }
                is Result.Error -> {
                    _imageAlert.value = result.message!!
                    _editState.value = MyState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }
                is Result.Loading -> {
                    _editState.value = MyState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }


}