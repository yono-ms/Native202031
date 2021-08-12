package com.example.native202031

import com.example.native202031.network.CommitModel

data class CommitItem(
    val message: String,
    val name: String,
    val date: String,
) {
    companion object {
        fun fromCommitModel(commitModel: CommitModel): CommitItem {
            val date = commitModel.commit.committer.date.fromIsoToDate()
            return CommitItem(
                message = commitModel.commit.message,
                name = commitModel.commit.committer.name,
                date = date.toBestString()
            )
        }
    }
}
