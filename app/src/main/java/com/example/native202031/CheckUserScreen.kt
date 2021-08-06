package com.example.native202031

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.native202031.ui.theme.Native202031Theme

@Composable
fun CheckUserScreen() {
    val viewModel: CheckUserViewModel = viewModel()
    CheckUserScreenBase(viewModel = viewModel)
}

@Composable
fun CheckUserScreenBase(viewModel: CheckUserViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val userName = viewModel.userName.collectAsState()
        val userNameLabel = viewModel.userNameLabel.collectAsState()
        val userNameIsError = viewModel.userNameIsError.collectAsState()
        TextField(
            value = userName.value,
            onValueChange = { viewModel.userNameChanged(it) },
            label = { Text(text = userNameLabel.value) },
            placeholder = { Text(text = "your login name") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = userNameIsError.value,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.check() }) {
            Text(text = "check!")
        }
    }

    val progress = viewModel.progress.collectAsState()
    if (progress.value) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    val showDialog = viewModel.showDialog.collectAsState()
    if (showDialog.value) {
        val dialogMessage = viewModel.dialogMessage.collectAsState()
        val dialogTitle = viewModel.dialogTitle.collectAsState()
        MessageDialog(text = dialogMessage.value, title = dialogTitle.value) {
            viewModel.dismissDialog()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckUserPreview() {
    Native202031Theme {
        CheckUserScreenBase(CheckUserViewModel())
    }
}
