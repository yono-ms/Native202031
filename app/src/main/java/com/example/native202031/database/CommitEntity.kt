package com.example.native202031.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.native202031.fromIsoToDate
import com.example.native202031.network.CommitModel
import com.example.native202031.toBestString
import java.util.*

@Entity(tableName = "commit_entity")
data class CommitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "day_of_month") val dayOfMonth: Int,
    @ColumnInfo(name = "committer_date") val committerDate: Long,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "login") val login: String,
    @ColumnInfo(name = "repo") val repo: String,
) {
    companion object {
        fun fromCommit(commitModel: CommitModel, login: String, repo: String): CommitEntity {
            val date = commitModel.commit.committer.date.fromIsoToDate()
            val cal = Calendar.getInstance(Locale.getDefault()).apply { time = date }
            return CommitEntity(
                0,
                cal[Calendar.YEAR],
                cal[Calendar.MONTH],
                cal[Calendar.DAY_OF_MONTH],
                date.time,
                commitModel.commit.message,
                login,
                repo,
            )
        }
    }

    fun getCommitterDateString(): String {
        return Date(committerDate).toBestString()
    }
}