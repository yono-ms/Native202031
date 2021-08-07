package com.example.native202031

import androidx.compose.foundation.layout.*
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
    HomeContent(userName = userName) {
        viewModel.checkUser()
    }
}

@Composable
fun HomeContent(userName: String, onCheckUser: () -> Unit) {
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
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Native202031Theme {
        HomeContent("user name") {}
    }
}