package com.example.native202031.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Singleton
    @Provides
    fun provideCommitDao(appDB: AppDB): CommitDao {
        return appDB.commitDao()
    }

    @Singleton
    @Provides
    fun provideAppDB(@ApplicationContext context: Context): AppDB {
        return Room.databaseBuilder(
            context,
            AppDB::class.java,
            "AppDB"
        ).fallbackToDestructiveMigration().build()
    }
}