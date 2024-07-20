package com.chapeaumoineau.miavortoj.feature.dictionary.presentation.dictionnaries

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chapeaumoineau.miavortoj.R
import com.chapeaumoineau.miavortoj.feature.dictionary.presentation.dictionnaries.components.DeleteDialog
import com.chapeaumoineau.miavortoj.feature.dictionary.presentation.dictionnaries.components.DictionaryItem
import com.chapeaumoineau.miavortoj.feature.dictionary.presentation.dictionnaries.components.OrderDictionariesSection
import com.chapeaumoineau.miavortoj.presentation.Screen
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DictionariesScreen(navController: NavController, viewModel:DictionariesViewModel = hiltViewModel()) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val listState = rememberLazyListState()

    val isFloatingButtonVisible by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
        }
    }

    val deleteTextToEnter = stringResource(R.string.dictionary_screen_delete_to_enter)

    BackHandler(enabled = (state.dictionaryEdit != -1)) {
        if(state.dictionaryEdit!=-1) viewModel.onEvent(DictionariesEvent.ToggleEditMode(-1))
    }

    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(visible = isFloatingButtonVisible, enter = scaleIn(), exit = scaleOut()) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.AddEditDictionaryScreen.route)
                    },
                    backgroundColor = MaterialTheme.colors.primary,
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add dictionary")
                }
            }
    },
        scaffoldState = scaffoldState
    ) {
        state.dictionaryDelete?.let { it1 ->
            DeleteDialog(isVisible = state.isDeleteDialogVisible, it1)
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.dictionary_screen_title),
                    style = MaterialTheme.typography.h4)
                IconButton(onClick = {
                    viewModel.onEvent(DictionariesEvent.ToggleOrderSection)
                }) {
                    Icon(imageVector = Icons.Default.Sort,
                        contentDescription = "Sort")
                }
            }

            AnimatedVisibility(visible = state.isOrderSectionVisible, enter = fadeIn() + slideInVertically(), exit = fadeOut() + slideOutVertically()) {
                OrderDictionariesSection(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), dictionaryOrder = state.dictionaryOrder, onOrderChange = {
                    viewModel.onEvent(DictionariesEvent.Order(it))
                })
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState
            ) {
                items(state.dictionaries, key = { it.dictionary.id!! } ) { dictionary ->
                    DictionaryItem(
                        dictionary = dictionary.dictionary,
                        dictionaryEdited = state.dictionaryEdit,
                        onMoreClick = {
                            viewModel.onEvent(DictionariesEvent.ToggleEditMode(dictionary.dictionary.id))

                        },
                        onEditClick = {
                            viewModel.onEvent(DictionariesEvent.ToggleEditMode(-1))
                            navController.navigate(Screen.AddEditDictionaryScreen.route + "?dictionaryId=${dictionary.dictionary.id}")
                        },
                        onDeleteClick = {
                            viewModel.onEvent(DictionariesEvent.ToggleDeleteDialog(dictionary.dictionary, deleteTextToEnter))
                        },
                        onBackClick = {
                            viewModel.onEvent(DictionariesEvent.ToggleEditMode(-1))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement()
                            .clickable(
                                onClick = {
                                    viewModel.onEvent(DictionariesEvent.ToggleEditMode(-1))
                                    navController.navigate(Screen.WordsScreen.route + "?dictionaryId=${dictionary.dictionary.id}")
                                }
                            )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}