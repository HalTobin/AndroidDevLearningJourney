package com.chapeaumoineau.pocketlocker.feature.content

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.chapeaumoineau.pocketlocker.feature.content.component.DeleteConfirmation
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalFoundationApi::class)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun ContentScreen(
    navController: NavController,
    state: ContentState,
    onEvent: (ContentEvent) -> Unit,
    uiEvent: SharedFlow<ContentViewModel.UiEvent>
) {

    val context = LocalContext.current

    val mediaPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uriList ->
        onEvent(ContentEvent.AddElements(uriList, "123456"))
    }

    var elementToDelete by remember { mutableStateOf("") }
    var deleteConfirmation by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        uiEvent.collectLatest { event ->
            when (event) {
                else -> {}
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            SmallFloatingActionButton(
                modifier = Modifier.size(64.dp),
                onClick = { mediaPicker.launch(PickVisualMediaRequest(
                    mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                )) }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->

        state.loadingData?.let {
            Dialog(onDismissRequest = { }) {
                Surface(shape = RoundedCornerShape(8.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(128.dp).padding(16.dp))
                    }
                }
            }
        }

        if (deleteConfirmation) DeleteConfirmation(
            onDismiss = { deleteConfirmation = false },
            onDelete = {
                deleteConfirmation = false
                onEvent(ContentEvent.DeleteElement(elementToDelete))
            }
        )

        Column(modifier= Modifier.fillMaxSize()) {
            state.album?.let { album ->
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                    text = album.title,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(state.elements) { item ->
                    SubcomposeAsyncImage(
                        modifier = Modifier
                            .size(128.dp)
                            .combinedClickable(
                                onClick = {},
                                onLongClick = {
                                    elementToDelete = item
                                    deleteConfirmation = true
                                }
                            ),
                        model = ImageRequest
                            .Builder(LocalContext.current)
                            .data(item)
                            .crossfade(true)
                            .build(),
                        contentScale = ContentScale.Crop,
                        contentDescription = null)
                }
            }
        }
    }

}