package com.example.native202031

import com.example.native202031.database.CommitDao
import javax.inject.Inject

class AppCache @Inject constructor(
    private val commitDao: CommitDao
)