package com.example.native202031

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun MessageDialog(text: String, title: String? = null, onClick: () -> Unit) {

    var titleText: @Composable (() -> Unit)? = null
    title?.let {
        titleText = { Text(text = it) }
    }

    val okText = stringResource(id = android.R.string.ok)

    AlertDialog(
        onDismissRequest = { /* ignore */ },
        confirmButton = { TextButton(onClick = onClick) { Text(text = okText) } },
        dismissButton = null,
        title = titleText,
        text = { Text(text = text) },
    )
}
