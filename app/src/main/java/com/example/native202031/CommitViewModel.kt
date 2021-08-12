package com.example.native202031

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.native202031.database.AppCache
import com.example.native202031.network.CommitModel
import com.example.native202031.network.ServerAPI
import com.example.native202031.preference.AppPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CommitViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val appPrefs: AppPrefs,
    private val appCache: AppCache,
    private val serverAPI: ServerAPI
) : BaseViewModel() {

    private val _userName = MutableStateFlow(Date().toBestString())
    val userName: StateFlow<String> = _userName

    private val _repo = MutableStateFlow("")
    val repo: StateFlow<String> = _repo

    private val _commits = MutableStateFlow(listOf<CommitItem>())
    val commits: StateFlow<List<CommitItem>> = _commits

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
                val commits = serverAPI.getDecode(
                    serverAPI.getCommitsUrl(userName.value, repo.value),
                    ListSerializer(CommitModel.serializer())
                )
                commits.map { CommitItem.fromCommitModel(it) }
            }.onSuccess {
                _commits.value = it
            }.onFailure {
                logger.error("init", it)
                showDialog(it.message, it.javaClass.simpleName)
            }.also {
                hideProgress()
            }
        }
    }
}