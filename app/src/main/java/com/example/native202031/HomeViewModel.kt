package com.example.native202031

import androidx.lifecycle.viewModelScope
import com.example.native202031.network.RepoModel
import com.example.native202031.network.ServerAPI
import com.example.native202031.network.UserModel
import com.example.native202031.preference.AppPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appPrefs: AppPrefs,
    private val serverAPI: ServerAPI
) : BaseViewModel() {

    private val _userName = MutableStateFlow(Date().toBestString())
    val userName: StateFlow<String> = _userName

    private val _repositories = MutableStateFlow(listOf<RepositoryItem>())
    val repositories: StateFlow<List<RepositoryItem>> = _repositories

    lateinit var repo: String

    fun clickRepository(repository: RepositoryItem) {
        logger.info("clickRepository $repository")
        viewModelScope.launch {
            repo = repository.name
            sendDestScreen(
                DestScreen(
                    route = DestScreen.Route.COMMIT,
                    args = repository.name
                )
            )
        }
    }

    fun checkUser() {
        logger.info("checkUser")
        viewModelScope.launch {
            sendDestScreen(DestScreen(route = DestScreen.Route.CHECK_USER))
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
        val userModel = serverAPI.getDecode(
            serverAPI.getUsersUrl(userName),
            UserModel.serializer()
        )
        val repoModel = serverAPI.getDecode(
            userModel.reposUrl,
            ListSerializer(RepoModel.serializer())
        )
        return repoModel.map { RepositoryItem.fromRepoModel(it) }
    }

    init {
        logger.info("init")
        viewModelScope.launch {
            kotlin.runCatching {
                showProgress()
                appPrefs.getUserName()?.let {
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