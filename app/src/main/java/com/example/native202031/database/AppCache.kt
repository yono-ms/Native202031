package com.example.native202031.database

import com.example.native202031.network.CommitModel
import com.example.native202031.network.ServerAPI
import kotlinx.serialization.builtins.ListSerializer
import java.util.*
import javax.inject.Inject

class AppCache @Inject constructor(
    private val commitDao: CommitDao,
    private val serverAPI: ServerAPI
) {
    suspend fun getCommits(login: String, repo: String) {
        if (isOld(login, repo)) {
            commitDao.deleteAll()
            val commits = serverAPI.getDecode(
                serverAPI.getCommitsUrl(login, repo),
                ListSerializer(CommitModel.serializer())
            )
            val entities = commits.map { CommitEntity.fromCommit(it, login, repo) }
            commitDao.insertAll(*entities.toTypedArray())
        }
    }

    private suspend fun isOld(login: String, repo: String): Boolean {
        if (commitDao.getCount() > 0) {
            commitDao.getMaxCommitterDate(login, repo)?.let {
                val diff = Date().time - it
                return diff > 1000 * 60 * 60 * 24 * 7
            }
        }
        return true
    }
}