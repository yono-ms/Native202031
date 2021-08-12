package com.example.native202031.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CommitDao {
    @Insert
    suspend fun insertAll(vararg commitEntity: CommitEntity)

    @Delete
    suspend fun delete(commitEntity: CommitEntity)

    @Query("SELECT * FROM commit_entity")
    suspend fun getAll(): List<CommitEntity>

    @Query("SELECT * FROM commit_entity")
    fun getAllFlow(): Flow<List<CommitEntity>>

    @Query("SELECT COUNT(*) FROM commit_entity")
    suspend fun getCount(): Int

    @Query("SELECT MAX(committer_date) FROM commit_entity WHERE login=:login AND repo=:repo")
    suspend fun getMaxCommitterDate(login: String, repo: String): Long?

    @Transaction
    suspend fun deleteAll() {
        getAll().toList().forEach { delete(it) }
    }
}