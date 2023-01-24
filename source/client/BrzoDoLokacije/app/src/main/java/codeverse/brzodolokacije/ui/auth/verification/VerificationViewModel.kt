package codeverse.brzodolokacije.ui.auth.verification

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import codeverse.brzodolokacije.data.models.VerifyUser
import codeverse.brzodolokacije.domain.use_case.auth.VerificationUseCase
import codeverse.brzodolokacije.utils.MyState
import codeverse.brzodolokacije.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(private val verificationUseCase: VerificationUseCase): ViewModel() {

    private val _state = mutableStateOf(MyState<Unit>())
    val state: State<MyState<Unit>> = _state

    fun verifyUser(email : String, token: String) {

        val verifyUser = VerifyUser(email, token)

        verificationUseCase(verifyUser).onEach { result ->
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