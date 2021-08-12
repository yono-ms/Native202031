package com.example.native202031

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.native202031.ui.theme.Native202031Theme

@Composable
fun SignInScreen(viewModel: SignInViewModel = hiltViewModel()) {
    val userName by viewModel.userName.collectAsState()

    SignInContent(
        userName = userName,
        onUserNameChanged = { viewModel.userNameChanged(it) },
        onSignIn = { viewModel.signIn() })
}

@Composable
fun SignInContent(userName: String, onUserNameChanged: (String) -> Unit, onSignIn: () -> Unit) {

    var password by remember {
        mutableStateOf("")
    }
    var passwordVisibility by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "SignIn Screen!")
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = userName,
            onValueChange = { onUserNameChanged(it) },
            label = { Text(text = "User Name") },
            placeholder = { Text(text = "your login name") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image =
                    if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector = image, contentDescription = null)

                }
            },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            onSignIn()
        }) {
            Text(text = "sign in")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInPreview() {
    Native202031Theme {
        SignInContent(userName = "google", onUserNameChanged = {}, onSignIn = {})
    }
}