package com.example.native202031.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CommitEntity::class], version = 1, exportSchema = false)
abstract class AppDB : RoomDatabase() {
    abstract fun commitDao(): CommitDao
}