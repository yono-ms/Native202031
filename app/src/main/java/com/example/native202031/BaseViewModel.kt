package com.example.native202031

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel(), LifecycleObserver {

    protected val logger: Logger by lazy { LoggerFactory.getLogger(javaClass.simpleName) }

    private val _destScreen = Channel<DestScreen>()
    val destScreen: ReceiveChannel<DestScreen> = _destScreen

    protected suspend fun sendDestScreen(destScreen: DestScreen) {
        _destScreen.send(destScreen)
    }

    private val _progress = MutableStateFlow(false)
    val progress: StateFlow<Boolean> = _progress

    protected fun showProgress() {
        _progress.value = true
    }

    protected fun hideProgress() {
        _progress.value = false
    }

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private val _dialogMessage = MutableStateFlow("")
    val dialogMessage: StateFlow<String> = _dialogMessage

    private val _dialogTitle = MutableStateFlow<String?>(null)
    val dialogTitle: StateFlow<String?> = _dialogTitle

    protected fun showDialog(message: String?, title: String? = null) {
        _dialogMessage.value = message ?: "NO message."
        _dialogTitle.value = title
        _showDialog.value = true
    }

    fun dismissDialog() {
        logger.info("dismissDialog")
        viewModelScope.launch { _showDialog.value = false }
    }
}