package com.chapeaumoineau.pocketlocker.feature.content.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.chapeaumoineau.pocketlocker.R
import com.chapeaumoineau.pocketlocker.ui.theme.DarkRed

@Composable
fun DeleteConfirmation(
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
) {

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(8.dp)) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(modifier = Modifier.padding(bottom = 8.dp),
                    text = stringResource(id = R.string.delete_confirmation))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    TextButton(onClick = onDelete) {
                        Text(text = stringResource(id = R.string.delete), color = DarkRed)
                    }
                }
            }
        }
    }

}