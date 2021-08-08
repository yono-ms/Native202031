package com.example.native202031

import com.example.native202031.network.RepoModel

data class RepositoryItem(
    val name: String,
    val fullName: String,
    val updatedAt: String
) {
    companion object {
        fun fromRepoModel(repoModel: RepoModel): RepositoryItem {
            val date = repoModel.updatedAt.fromIsoToDate()
//            val cal = Calendar.getInstance(Locale.getDefault()).apply { time = date }
            return RepositoryItem(
                name = repoModel.name,
                fullName = repoModel.fullName,
                updatedAt = date.toBestString()
            )
        }
    }
}
