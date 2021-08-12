package com.example.native202031

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.native202031.ui.theme.Native202031Theme

@Composable
fun CommitScreen(viewModel: CommitViewModel = hiltViewModel()) {
    val userName by viewModel.userName.collectAsState()
    val repo by viewModel.repo.collectAsState()
    CommitContent(userName = userName, repo = repo)

    val progress by viewModel.progress.collectAsState()

    FullScreenProgress(progress = progress)

    val showDialog by viewModel.showDialog.collectAsState()
    val dialogMessage by viewModel.dialogMessage.collectAsState()
    val dialogTitle by viewModel.dialogTitle.collectAsState()

    MessageDialog(
        showDialog = showDialog,
        text = dialogMessage,
        title = dialogTitle
    ) {
        viewModel.dismissDialog()
    }
}

@Composable
fun CommitContent(userName: String, repo: String) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = userName)
        Text(text = repo)
    }
}

@Preview(showBackground = true)
@Composable
fun CommitPreview() {
    Native202031Theme {
        CommitContent("user name", "repo")
    }
}