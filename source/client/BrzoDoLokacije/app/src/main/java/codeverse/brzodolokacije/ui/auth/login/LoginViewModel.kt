package codeverse.brzodolokacije.ui.auth.login

import android.text.TextUtils
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import codeverse.brzodolokacije.data.models.LoginResponse
import codeverse.brzodolokacije.data.models.LoginUser
import codeverse.brzodolokacije.domain.use_case.auth.LoginUseCase
import codeverse.brzodolokacije.utils.MyState
import codeverse.brzodolokacije.utils.Result
import codeverse.brzodolokacije.utils.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
): ViewModel(){

    private val _state = mutableStateOf(MyState<LoginResponse>())
    val state: State<MyState<LoginResponse>> = _state


    fun login(username: String, password: String){

        val loginUser = LoginUser(username, password)

        loginUseCase(loginUser).onEach { result ->
            when (result) {
                is Result.Success -> {
                    _state.value = MyState<LoginResponse>(success = true, data = result.data)
                }
                is Result.Error -> {
                    _state.value = MyState<LoginResponse>(
                        error = result.message ?: "An unexpected error occured"
                    )
                }
                is Result.Loading -> {
                    _state.value = MyState<LoginResponse>(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun validateCredentials(username: String, password: String) : Pair<Boolean, String> {
        var temp = Pair(true,"")

        //ispitujemo username i password

        temp = Validation.validateUsername(username)
        if (!temp.first){
            return temp
        }
        temp = Validation.validatePassword(password)
        if (!temp.first){
            return temp
        }

        return Pair(true, "")
    }
}