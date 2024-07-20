package com.chapeaumoineau.pocketlocker.feature.home_lock.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.chapeaumoineau.pocketlocker.R
import com.chapeaumoineau.pocketlocker.ui.composable.SecretTextField

@Composable
fun OpenAlbumDialog(
    fail: Boolean,
    onDismiss: () -> Unit,
    onOpen: (String) -> Unit
) {

    var key by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(8.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SecretTextField(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    isError = fail,
                    label = stringResource(id = R.string.password),
                    value = key,
                    onValueChange = { key = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    enabled = (key.isNotBlank()),
                    onClick = { onOpen(key) }
                ) {
                    Text(modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(id = R.string.open).uppercase())
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

}