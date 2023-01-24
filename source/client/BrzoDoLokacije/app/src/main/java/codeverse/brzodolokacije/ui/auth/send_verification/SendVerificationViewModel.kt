package codeverse.brzodolokacije.ui.auth.send_verification

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import codeverse.brzodolokacije.data.models.RegisterUser
import codeverse.brzodolokacije.data.models.User
import codeverse.brzodolokacije.data.models.VerificationCodeRequest
import codeverse.brzodolokacije.domain.use_case.auth.SendVerificationUseCase
import codeverse.brzodolokacije.utils.MyState
import codeverse.brzodolokacije.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SendVerificationViewModel @Inject constructor(private val sendVerificationUseCase: SendVerificationUseCase) : ViewModel() {

    private val _state = mutableStateOf(MyState<Unit>())
    val state: State<MyState<Unit>> = _state

    fun sendVerification(email : String, isForgotPassword: Boolean) {

        val verificationCodeRequest = VerificationCodeRequest(email, isForgotPassword)

        sendVerificationUseCase(verificationCodeRequest).onEach { result ->
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

}