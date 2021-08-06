package com.example.native202031

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.native202031.ui.theme.Native202031Theme


@Composable
fun HomeScreen(viewModel: MainViewModel = MainViewModel()) {
    val userName = viewModel.userName.collectAsState()
    var name by remember {
        mutableStateOf("")
    }
    val welcomeMessage = viewModel.welcomeMessage.collectAsState()
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = userName.value)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = name)
        Spacer(modifier = Modifier.height(16.dp))
        welcomeMessage.value.also {
            Text(text = it)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            name = "pushed!"
            viewModel.homeSignIn()
            viewModel.showDialog.value = true
        }) {
            Text(text = "Button to NextScreen")
        }
    }
    val progress = viewModel.progress.collectAsState()
    if (progress.value) {
        CircularProgressIndicator()
    }
    val showDialog = viewModel.showDialog.collectAsState()
    if (showDialog.value) {
        MessageDialog(text = "message") {
            viewModel.showDialog.value = false
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Native202031Theme {
        HomeScreen()
    }
}