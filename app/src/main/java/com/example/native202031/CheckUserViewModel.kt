package com.example.native202031

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.native202031.network.ServerAPI
import com.example.native202031.network.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CheckUserViewModel(application: Application) : BaseViewModel(application) {

    init {
        getUserName()?.let {
            _userName.value = it
        }
    }

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _userNameLabel = MutableStateFlow("Input User Name.")
    val userNameLabel: StateFlow<String> = _userNameLabel

    private val _userNameIsError = MutableStateFlow(true)
    val userNameIsError: StateFlow<Boolean> = _userNameIsError

    fun userNameChanged(value: String) {
        logger.info("userNameChanged $value")
        viewModelScope.launch {
            if (value.isBlank()) {
                _userNameLabel.value = "Input User Name."
                _userNameIsError.value = true
            } else {
                _userNameLabel.value = "User Name"
                _userNameIsError.value = false
            }
            _userName.value = value
        }
    }

    fun check() {
        logger.info("check")
        if (progress.value) {
            logger.info("...busy.")
            return
        }

        viewModelScope.launch {
            if (userNameIsError.value) {
                logger.info("...need user name.")
                showDialog(userNameLabel.value)
                return@launch
            }

            kotlin.runCatching {
                showProgress()
                ServerAPI.getDecode(ServerAPI.getUsersUrl(userName.value), UserModel.serializer())
            }.onSuccess { userModel ->
                logger.debug("$userModel")
                setUserName(userName.value)
                sendDestScreen(DestScreen.BACK)
            }.onFailure {
                logger.error("check", it)
                showDialog(it.message, it.javaClass.simpleName)
            }.also {
                hideProgress()
            }
        }
    }
}