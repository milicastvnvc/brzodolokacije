package codeverse.brzodolokacije.ui.auth.register

import android.text.TextUtils
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import codeverse.brzodolokacije.data.models.RegisterUser
import codeverse.brzodolokacije.data.models.User
import codeverse.brzodolokacije.domain.use_case.auth.RegisterUseCase
import codeverse.brzodolokacije.utils.MyState
import codeverse.brzodolokacije.utils.Result
import codeverse.brzodolokacije.utils.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase): ViewModel(){

    private val _state = mutableStateOf(MyState<User>())
    val state: State<MyState<User>> = _state


    fun register(firstName : String, lastName: String,
                 username: String, email: String,
                 password: String) {

        val registerUser = RegisterUser(username,password,email,firstName,lastName)

        registerUseCase(registerUser).onEach { result ->
            when (result) {
                is Result.Success -> {
                    _state.value = MyState<User>(success = true,data= result.data)
                }
                is Result.Error -> {
                    _state.value = MyState<User>(
                        error = result.message ?: "An unexpected error occured"
                    )
                }
                is Result.Loading -> {
                    _state.value = MyState<User>(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun validateCredentials(username: String, password: String,
                            firstName: String, lastName: String,
                            email: String,passwordConfirm: String) : Pair<Boolean, String> {

        var temp = Pair(true,"")

        temp = Validation.validateName(firstName.trim(),lastName.trim())
        if (!temp.first){
            return temp
        }
        temp = Validation.validateEmail(email)
        if (!temp.first){
            return temp
        }

        temp = Validation.validateUsername(username)
        if (!temp.first){
            return temp
        }
        temp = Validation.validatePassword(password,passwordConfirm)
        if (!temp.first){
            return temp
        }

        return Pair(true, "")
    }


}