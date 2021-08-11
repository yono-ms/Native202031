package com.example.native202031.database

import androidx.room.*

@Dao
interface CommitDao {
    @Insert
    suspend fun insertAll(vararg commitEntity: CommitEntity)

    @Delete
    suspend fun delete(commitEntity: CommitEntity)

    @Query("SELECT * FROM commit_entity")
    suspend fun getAll(): List<CommitEntity>

    @Transaction
    suspend fun deleteAll() {
        getAll().toList().forEach { delete(it) }
    }
}