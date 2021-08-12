package com.example.native202031

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignInViewModel : BaseViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    fun userNameChanged(value: String) {
        viewModelScope.launch {
            logger.debug("user name = $value")
            _userName.value = value
        }
    }

    fun signIn() {
        logger.debug("signIn")
        viewModelScope.launch {
            sendDestScreen(DestScreen(route = DestScreen.Route.BACK))
        }
    }
}