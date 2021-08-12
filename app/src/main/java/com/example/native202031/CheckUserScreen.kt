package com.example.native202031

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.native202031.ui.theme.Native202031Theme

@Composable
fun CheckUserScreen(viewModel: CheckUserViewModel = hiltViewModel()) {

    val userName by viewModel.userName.collectAsState()
    val userNameLabel by viewModel.userNameLabel.collectAsState()
    val userNameIsError by viewModel.userNameIsError.collectAsState()

    CheckUserScreenContent(
        userName = userName,
        userNameLabel = userNameLabel,
        userNameIsError = userNameIsError,
        onUserNameChanged = { viewModel.userNameChanged(it) },
        onCheck = { viewModel.check() }
    )

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
fun CheckUserScreenContent(
    userName: String,
    userNameLabel: String,
    userNameIsError: Boolean,
    onUserNameChanged: (String) -> Unit,
    onCheck: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = userName,
            onValueChange = { onUserNameChanged(it) },
            label = { Text(text = userNameLabel) },
            placeholder = { Text(text = "your login name") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = userNameIsError,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onCheck() }) {
            Text(text = "check!")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckUserPreview() {
    Native202031Theme {
        CheckUserScreenContent(
            userName = "google",
            userNameLabel = "LABEL",
            userNameIsError = true,
            onUserNameChanged = {},
            onCheck = {})
    }
}
