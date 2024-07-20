package com.chapeaumoineau.pocketlocker.feature.albums

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chapeaumoineau.pocketlocker.R
import com.chapeaumoineau.pocketlocker.data.repository.StorageDestination
import com.chapeaumoineau.pocketlocker.feature.home_lock.component.NewAlbumDialog
import com.chapeaumoineau.pocketlocker.feature.home_lock.component.OpenAlbumDialog
import com.chapeaumoineau.pocketlocker.ui.Screen
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun AlbumScreen(
    navController: NavController,
    state: AlbumState,
    onEvent: (AlbumEvent) -> Unit,
    uiEvent: SharedFlow<AlbumViewModel.UiEvent>
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var addDialog by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }

    var isPasswordGood by remember { mutableStateOf(false) }
    var id by remember { mutableIntStateOf(-1) }

    if (addDialog) NewAlbumDialog(
        onDismiss = { addDialog = false },
        onCreate = {
            addDialog = false
            onEvent(AlbumEvent.CreateAlbum(it.title, StorageDestination.Intern, it.key))
        }
    )

    if (openDialog && id != -1) OpenAlbumDialog(
        fail = isPasswordGood,
        onDismiss = { openDialog = false },
        onOpen = { key -> onEvent(AlbumEvent.OpenAlbum(id, key)) }
    )

    LaunchedEffect(key1 = true) {
        uiEvent.collectLatest { event ->
            when (event) {
                is AlbumViewModel.UiEvent.UnlockFail -> {
                    isPasswordGood = false
                    snackbarHostState.showSnackbar(context.getString(R.string.wrong_password))
                }
                is AlbumViewModel.UiEvent.UnlockSuccess -> {
                    openDialog = false
                    navController.navigate(Screen.AlbumContent.route + "?albumId=${event.albumId}" + "&key=${event.key}")
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            SmallFloatingActionButton(
                modifier = Modifier.size(64.dp),
                onClick = { addDialog = true }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize()) {
            if (state.albums.isNotEmpty()) LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                items(state.albums) { album ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.secondary)
                            .clickable {
                                album.id?.let { id = it }
                                openDialog = true
                            }
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(modifier = Modifier.weight(1f),
                            text = album.title)
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            else {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = R.string.no_album),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }

}