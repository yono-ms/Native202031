package com.example.native202031

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.native202031.ui.theme.Native202031Theme

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val userName by viewModel.userName.collectAsState()
    val repositories by viewModel.repositories.collectAsState()
    HomeContent(userName = userName, repositories = repositories, onClickItem = {
        viewModel.clickRepository(it)
    }) {
        viewModel.checkUser()
    }

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
fun HomeContent(
    userName: String,
    repositories: List<RepositoryItem>,
    onClickItem: (repository: RepositoryItem) -> Unit,
    onCheckUser: () -> Unit
) {
    var name by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = userName)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = name)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            name = "pushed!"
            onCheckUser()
        }) {
            Text(text = "Button to CheckUser")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            contentPadding = PaddingValues(0.dp, 0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(repositories, key = { item -> item.name }) { repository ->
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onClickItem(repository)
                    }) {
                    Text(text = repository.name, fontSize = 16.sp)
                    Text(text = repository.fullName, fontSize = 10.sp)
                    Text(text = repository.updatedAt, fontSize = 10.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Native202031Theme {
        val list = listOf(
            RepositoryItem(name = "name1", fullName = "fullName1", updatedAt = "updatedAt1"),
            RepositoryItem(name = "name2", fullName = "fullName2", updatedAt = "updatedAt2"),
        )
        HomeContent("user name", list, {}) {}
    }
}