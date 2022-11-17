package com.eastx7.films.uiutilities

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.eastx7.films.R
import com.eastx7.films.theme.VertFieldSpacer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogSearch(
    show: Boolean,
    title: String,
    onSearchTitleChanged: (String) -> Unit,
    onSearchQntChanged: (Int) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (show) {
        var textFilmTitle by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(""))
        }
        var textFilmQnt by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(""))
        }
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onConfirm)
                { Text(text = stringResource(R.string.apply).uppercase()) }
            },
            dismissButton = {
                TextButton(onClick = onDismiss)
                { Text(text = stringResource(R.string.cancel).uppercase()) }
            },
            title = { Text(text = title) },
            text = {
                Column {
                    TextField(
                        value = textFilmTitle,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = {
                            textFilmTitle = it
                            onSearchTitleChanged(it.text)
                        },
                        label = { Text(stringResource(R.string.title)) },
                        singleLine = true,
                    )

                    VertFieldSpacer()

                    TextField(
                        value = textFilmQnt,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = {
                            textFilmQnt = it
                            onSearchQntChanged(it.text.toInt())
                        },
                        label = { Text(stringResource(R.string.quantity)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                    )
                }
            }
        )
    }
}
