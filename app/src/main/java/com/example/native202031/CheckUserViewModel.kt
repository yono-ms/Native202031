package com.example.native202031

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.native202031.network.ServerAPI
import com.example.native202031.network.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CheckUserViewModel : ViewModel() {

    private val logger: Logger by lazy { LoggerFactory.getLogger(javaClass.simpleName) }

    private val _progress = MutableStateFlow(false)
    val progress: StateFlow<Boolean> = _progress

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private val _dialogMessage = MutableStateFlow("")
    val dialogMessage: StateFlow<String> = _dialogMessage

    private val _dialogTitle = MutableStateFlow<String?>(null)
    val dialogTitle: StateFlow<String?> = _dialogTitle

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
        if (_progress.value) {
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
                _progress.value = true
                ServerAPI.getDecode(ServerAPI.getUsersUrl(userName.value), UserModel.serializer())
            }.onSuccess { userModel ->
                logger.debug("$userModel")
            }.onFailure {
                logger.error("check", it)
                showDialog(it.message, it.javaClass.simpleName)
            }.also {
                _progress.value = false
            }
        }
    }

    private fun showDialog(message: String?, title: String? = null) {
        _dialogMessage.value = message ?: "NO message."
        _dialogTitle.value = title
        _showDialog.value = true
    }

    fun dismissDialog() {
        logger.info("dismissDialog")
        viewModelScope.launch { _showDialog.value = false }
    }
}