package com.example.native202031

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel : BaseViewModel() {

    private val _userName = MutableStateFlow(Date().toBestString())
    val userName: StateFlow<String> = _userName

    fun setUser(userName: String) {
        logger.info("setUser $userName")
        viewModelScope.launch {
            _userName.value = userName
        }
    }

    fun checkUser() {
        logger.info("checkUser")
        viewModelScope.launch {
            sendDestScreen(DestScreen.CHECK_USER)
        }
    }
}