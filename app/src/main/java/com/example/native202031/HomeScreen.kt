package com.example.native202031

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.native202031.ui.theme.Native202031Theme

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val userName by viewModel.userName.collectAsState()
    val repositories by viewModel.repositories.collectAsState()
    HomeContent(userName = userName, repositories = repositories) {
        viewModel.checkUser()
    }
}

@Composable
fun HomeContent(userName: String, repositories: List<String>, onCheckUser: () -> Unit) {
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
        LazyColumn(contentPadding = PaddingValues(16.dp, 8.dp)) {
            items(repositories) { repository ->
                Text(text = repository)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Native202031Theme {
        HomeContent("user name", listOf("111", "222")) {}
    }
}