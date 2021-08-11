package com.example.native202031

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CommitViewModel @Inject constructor(
    private val appPrefs: AppPrefs,
    private val appCache: AppCache
) : BaseViewModel() {

    private val _userName = MutableStateFlow(Date().toBestString())
    val userName: StateFlow<String> = _userName

    init {
        logger.info("init")
        viewModelScope.launch {
            kotlin.runCatching {
                showProgress()
                appPrefs.getUserName()?.let {
                    _userName.value = it
                }
            }.onFailure {
                logger.error("init", it)
            }.also {
                hideProgress()
            }
        }
    }

    fun test() {
        logger.debug("test")
    }
}