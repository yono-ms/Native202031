package com.example.native202031

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.native202031.database.AppCache
import com.example.native202031.preference.AppPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CommitViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val appPrefs: AppPrefs,
    private val appCache: AppCache
) : BaseViewModel() {

    private val _userName = MutableStateFlow(Date().toBestString())
    val userName: StateFlow<String> = _userName

    private val _repo = MutableStateFlow("")
    val repo: StateFlow<String> = _repo

    init {
        logger.info("init")
        viewModelScope.launch {
            kotlin.runCatching {
                showProgress()
                appPrefs.getUserName()?.let {
                    _userName.value = it
                }
                savedStateHandle.get<String>("repo")?.let {
                    _repo.value = it
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