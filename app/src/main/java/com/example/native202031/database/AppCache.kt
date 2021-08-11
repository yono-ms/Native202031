package com.example.native202031.database

import javax.inject.Inject

class AppCache @Inject constructor(
    private val commitDao: CommitDao
)