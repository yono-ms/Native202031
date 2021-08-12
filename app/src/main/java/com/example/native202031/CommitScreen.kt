package com.example.native202031

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.native202031.ui.theme.Native202031Theme

@Composable
fun CommitScreen(viewModel: CommitViewModel = hiltViewModel()) {
    val userName by viewModel.userName.collectAsState()
    val repo by viewModel.repo.collectAsState()
    val commits by viewModel.commits.collectAsState()
    CommitContent(userName = userName, repo = repo, commits = commits)

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
fun CommitContent(userName: String, repo: String, commits: List<CommitItem>) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = userName)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = repo)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            contentPadding = PaddingValues(0.dp, 0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(commits) { commit ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = commit.message, fontSize = 16.sp)
                    Text(text = commit.name, fontSize = 10.sp)
                    Text(text = commit.date, fontSize = 10.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommitPreview() {
    Native202031Theme {
        val list = listOf(
            CommitItem(message = "message1", name = "name1", date = "date1"),
            CommitItem(message = "message2", name = "name2", date = "date2")
        )
        CommitContent("user name", "repo", list)
    }
}