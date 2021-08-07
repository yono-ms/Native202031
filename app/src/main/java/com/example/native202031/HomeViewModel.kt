package com.example.native202031

import androidx.lifecycle.viewModelScope
import com.example.native202031.network.RepoModel
import com.example.native202031.network.ServerAPI
import com.example.native202031.network.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import java.util.*

class HomeViewModel : BaseViewModel() {

    private val _userName = MutableStateFlow(Date().toBestString())
    val userName: StateFlow<String> = _userName

    fun setUser(userName: String) {
        logger.info("setUser $userName")
        viewModelScope.launch {
            _userName.value = userName
            kotlin.runCatching {
                showProgress()
                val userModel = ServerAPI.getDecode(
                    ServerAPI.getUsersUrl(userName),
                    UserModel.serializer()
                )
                val repoModel = ServerAPI.getDecode(
                    userModel.reposUrl,
                    ListSerializer(RepoModel.serializer())
                )
                repoModel.map { RepositoryItem.fromRepoModel(it) }
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

    fun checkUser() {
        logger.info("checkUser")
        viewModelScope.launch {
            sendDestScreen(DestScreen.CHECK_USER)
        }
    }

    private val _repositories = MutableStateFlow(listOf<RepositoryItem>())
    val repositories: StateFlow<List<RepositoryItem>> = _repositories


}