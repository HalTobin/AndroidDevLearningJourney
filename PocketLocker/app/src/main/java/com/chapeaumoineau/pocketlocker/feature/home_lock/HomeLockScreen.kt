package com.chapeaumoineau.pocketlocker.feature.home_lock

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chapeaumoineau.pocketlocker.R
import com.chapeaumoineau.pocketlocker.ui.Screen
import com.chapeaumoineau.pocketlocker.ui.composable.SecretTextField
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun HomeLockScreen(
    navController: NavController,
    state: HomeLockState,
    onEvent: (HomeLockEvent) -> Unit,
    uiEvent: SharedFlow<HomeLockViewModel.UiEvent>
) {

    var isPasswordError by remember { mutableStateOf(false) }
    var isCheckError by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                HomeLockViewModel.UiEvent.NotMatching -> isCheckError = true
                HomeLockViewModel.UiEvent.UnlockError -> isPasswordError = true
                HomeLockViewModel.UiEvent.UnlockSuccess -> navController.navigate(Screen.HomeAlbum.route)
            }
        }
    }
    Box {

        Text(
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 128.dp),
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineLarge
        )

        if (state.isFirst) {
            Column(modifier = Modifier
                .fillMaxWidth(0.75f)
                .align(Alignment.Center)) {
                SecretTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(id = R.string.password_new),
                    value = state.new1,
                    onValueChange = { onEvent(HomeLockEvent.SetField(HomeLockField.NEW_PASSWORD, it)) }
                )
                SecretTextField(
                    modifier = Modifier.fillMaxWidth(),
                    isError = (state.new1 != state.new2) || isCheckError,
                    label = stringResource(id = R.string.password_check),
                    value = state.new2,
                    onValueChange = { onEvent(HomeLockEvent.SetField(HomeLockField.CHECK_PASSWORD, it)) }
                )
            }
            Button(
                modifier = Modifier.align(Alignment.BottomCenter),
                enabled = (state.new1.isNotBlank() && state.new2.isNotBlank()) && (state.new1 == state.new2),
                onClick = {
                    isCheckError = false
                    onEvent(HomeLockEvent.CreatePassword(state.new1, state.new2))
                }
            ) {
                Text(modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(id = R.string.save).uppercase())
            }
        }
        else {
            SecretTextField(
                modifier = Modifier.fillMaxWidth(0.75f).align(Alignment.Center),
                isError = isPasswordError,
                label = stringResource(id = R.string.password),
                value = state.entry,
                onValueChange = { onEvent(HomeLockEvent.SetField(HomeLockField.PASSWORD, it)) }
            )
            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                onClick = {
                    isPasswordError = false
                    onEvent(HomeLockEvent.UnlockApp(state.entry))
                }
            ) {
                Text(modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(id = R.string.open).uppercase())
            }
        }
    }

}