package com.example.native202031

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class MainViewModel : ViewModel() {

    private val logger: Logger by lazy { LoggerFactory.getLogger(javaClass.simpleName) }

    private val _destScreen = Channel<DestScreen>()
    val destScreen: ReceiveChannel<DestScreen> = _destScreen

    private val _progress = MutableStateFlow(false)
    val progress: StateFlow<Boolean> = _progress

    val showDialog = MutableStateFlow(false)

    private val _welcomeMessage = MutableStateFlow("Hello world!")
    val welcomeMessage: StateFlow<String> = _welcomeMessage

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    fun userNameChanged(value: String) {
        viewModelScope.launch {
            logger.debug("user name = $value")
            _userName.value = value
        }
    }

    init {
        logger.info("init")
        viewModelScope.launch {
            while (true) {
                delay(1000)
                _welcomeMessage.value = Date().toBestString()
            }
        }
    }

    fun homeSignIn() {
        logger.info("homeSignIn")
        viewModelScope.launch {
            _progress.value = true
            delay(2000)
            _progress.value = false
            _destScreen.send(DestScreen.SIGN_IN)
        }
    }

    fun popBackStack() {
        logger.info("popBackStack")
        viewModelScope.launch {
            _destScreen.send(DestScreen.BACK)
        }
    }
}
