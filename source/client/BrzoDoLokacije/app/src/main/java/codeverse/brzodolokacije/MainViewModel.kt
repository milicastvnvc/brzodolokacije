package codeverse.brzodolokacije

import androidx.lifecycle.ViewModel
import codeverse.brzodolokacije.utils.managers.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userManager: UserManager): ViewModel() {

    val current_user = userManager.getUser()
}