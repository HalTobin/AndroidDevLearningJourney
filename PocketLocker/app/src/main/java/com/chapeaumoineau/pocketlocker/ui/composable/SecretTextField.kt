package com.chapeaumoineau.pocketlocker.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun SecretTextField(
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {

    var isHidden by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier,
        label = { Text(text = label) },
        isError = isError,
        singleLine = true,
        visualTransformation =  if (isHidden) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Password
        ),
        trailingIcon = {
            IconButton(onClick = { isHidden = !isHidden }) {
                Icon(
                    imageVector = if (isHidden) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = null
                )
            }
        },
        value = value,
        onValueChange = { onValueChange(it) }
    )

}