package com.example.native202031

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.native202031.network.RepoModel
import com.example.native202031.network.ServerAPI
import com.example.native202031.network.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import java.util.*

class HomeViewModel(application: Application) : BaseViewModel(application) {

    private val _userName = MutableStateFlow(Date().toBestString())
    val userName: StateFlow<String> = _userName

    private val _repositories = MutableStateFlow(listOf<RepositoryItem>())
    val repositories: StateFlow<List<RepositoryItem>> = _repositories

    fun clickRepository(repository: RepositoryItem) {
        logger.info("clickRepository $repository")
        viewModelScope.launch {
            sendDestScreen(DestScreen.CHECK_USER)
        }
    }

    fun checkUser() {
        logger.info("checkUser")
        viewModelScope.launch {
            sendDestScreen(DestScreen.CHECK_USER)
        }
    }

    fun setUser(userName: String) {
        logger.info("setUser $userName")
        viewModelScope.launch {
            _userName.value = userName
            kotlin.runCatching {
                showProgress()
                getRepositoryItems(userName)
            }.onSuccess {
                _repositories.value = it
            }.onFailure {
                logger.error("setUser", it)
                showDialog(it.message, it.javaClass.simpleName)
            }.also {
                hideProgress()
            }
        }
    }

    private suspend fun getRepositoryItems(userName: String): List<RepositoryItem> {
        val userModel = ServerAPI.getDecode(
            ServerAPI.getUsersUrl(userName),
            UserModel.serializer()
        )
        val repoModel = ServerAPI.getDecode(
            userModel.reposUrl,
            ListSerializer(RepoModel.serializer())
        )
        return repoModel.map { RepositoryItem.fromRepoModel(it) }
    }

    init {
        viewModelScope.launch {
            kotlin.runCatching {
                showProgress()
                getUserName()?.let {
                    _userName.value = it
                    _repositories.value = getRepositoryItems(it)
                }
            }.onFailure {
                logger.error("init", it)
                showDialog(it.message, it.javaClass.simpleName)
            }.also {
                hideProgress()
            }
        }
    }
}