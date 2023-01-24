package codeverse.brzodolokacije.ui.auth.reset_password

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import codeverse.brzodolokacije.data.models.ResetPasswordData
import codeverse.brzodolokacije.domain.use_case.auth.ResetPasswordUseCase
import codeverse.brzodolokacije.utils.MyState
import codeverse.brzodolokacije.utils.Result
import codeverse.brzodolokacije.utils.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(private val resetPasswordUseCase: ResetPasswordUseCase):ViewModel() {

    private val _state = mutableStateOf(MyState<Unit>())
    val state: State<MyState<Unit>> = _state

    fun sendVerification(email : String, password: String, passwordConfirmation: String, token: String) {

        val resetPasswordData = ResetPasswordData(email, password, passwordConfirmation, token)

        resetPasswordUseCase(resetPasswordData).onEach { result ->
            when (result) {
                is Result.Success -> {
                    _state.value = MyState<Unit>(success = true)
                }
                is Result.Error -> {
                    _state.value = MyState<Unit>(
                        error = result.message ?: "An unexpected error occured"
                    )
                }
                is Result.Loading -> {
                    _state.value = MyState<Unit>(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun validatePasswords(password: String,passwordConfirm: String) : Pair<Boolean, String> {

        return Validation.validatePassword(password,passwordConfirm)
    }
}