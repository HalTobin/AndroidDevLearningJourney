package com.chapeaumoineau.miavortoj.feature.words.presentation.words

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.chapeaumoineau.miavortoj.R
import com.chapeaumoineau.miavortoj.presentation.Screen
import com.chapeaumoineau.miavortoj.feature.words.presentation.words.components.DeleteDialog
import com.chapeaumoineau.miavortoj.feature.words.presentation.words.components.OrderWordsSection
import com.chapeaumoineau.miavortoj.feature.words.presentation.words.components.WarningDialog
import com.chapeaumoineau.miavortoj.feature.words.presentation.words.components.WordItem
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WordsScreen(navController: NavController,
                viewModel:WordsViewModel = hiltViewModel()) {

    val listState = rememberLazyListState()

    val state = viewModel.state.value

    val dictionaryId = viewModel.dictionary.value.id
    val dictionaryTitle = viewModel.dictionary.value.title
    val dictionaryDescription = viewModel.dictionary.value.description
    val dictionaryLanguageId = viewModel.dictionary.value.languageIso

    val color = viewModel.language.value.getDarkColor()
    val flag = viewModel.language.value.flag

    val isFromSource = viewModel.isFromSource.value

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val isFloatingButtonVisible by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
        }
    }

    BackHandler(enabled = (state.wordEdit != -1)) {
        if(state.wordEdit!=-1) viewModel.onEvent(WordsEvent.ToggleEditMode(-1))
    }

    Scaffold(floatingActionButton = {
        AnimatedVisibility(visible = isFloatingButtonVisible, enter = scaleIn(), exit = scaleOut()) {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditWordScreen.route + "?dictionaryId=${dictionaryId}&dictionaryLanguage=${dictionaryLanguageId}")
                },
                backgroundColor = MaterialTheme.colors.primary) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add word")
            }
        }
    }, scaffoldState = scaffoldState) {
        state.wordDelete?.let { it1 ->
            DeleteDialog(isVisible = state.isDeleteDialogVisible, word = it1)
        }
        WarningDialog(isVisible = state.isWarningDialogVisible)

        Column(modifier = Modifier
            .fillMaxSize()
            .background(color)) {
            Row(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(flag),
                    contentDescription = "Flag",
                    modifier = Modifier
                        .size(65.dp)
                        .shadow(15.dp, CircleShape)
                        .clip(CircleShape)
                        .border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = CircleShape
                        )
                )
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(text = dictionaryTitle,
                        style = MaterialTheme.typography.h5)
                    Text(text = dictionaryDescription,
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 3)
                }
                IconButton(onClick = {
                    viewModel.onEvent(WordsEvent.ToggleOrderSection)
                }) {
                    Icon(imageVector = Icons.Default.Sort,
                        contentDescription = "Sort")
                }
            }
            AnimatedVisibility(visible = state.isOrderSectionVisible, enter = fadeIn() + slideInVertically(), exit = fadeOut() + slideOutVertically()) {
                Box(modifier = Modifier.padding(start = 16.dp)) {
                    OrderWordsSection(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp), wordOrder = state.wordOrder, onOrderChange = {
                        viewModel.onEvent(WordsEvent.Order(it))
                    })
                }
            }

            Button(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(8.dp)),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.dark_green)),
                onClick = {
                    if (state.words.isNotEmpty())
                        navController.navigate(Screen.QuizScreen.route + "?dictionaryId=${dictionaryId}")
                    else {
                        viewModel.onEvent(WordsEvent.ToggleWarningDialog)
                    }
                          },
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 8.dp,
                    end = 18.dp,
                    bottom = 8.dp
                )
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Start a quiz",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(
                    text = stringResource(R.string.words_start_quiz).uppercase(),
                    style = MaterialTheme.typography.subtitle1
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextField(modifier = Modifier.fillMaxWidth(),
                value = viewModel.search.value,
                onValueChange = {
                    viewModel.onEvent(WordsEvent.EnteredSearch(it))
                },
                label = {
                    Text(text = stringResource(R.string.search_hint), color = Color.LightGray)
                },
                maxLines = 1
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState
            ) {
                itemsIndexed(state.words) { index, word ->
                    WordItem(
                        word = word,
                        wordEdited = state.wordEdit,
                        isFromSource = isFromSource,
                        onMoreClick = {
                            viewModel.onEvent(WordsEvent.ToggleEditMode(word.id))
                        },
                        onEditClick = {
                            viewModel.onEvent(WordsEvent.ToggleEditMode(-1))
                            navController.navigate(Screen.AddEditWordScreen.route + "?dictionaryId=${word.dictionaryId}&wordId=${word.id}")
                        },
                        onDeleteClick = {
                            viewModel.onEvent(WordsEvent.ToggleDeleteDialog(word))
                        },
                        onBackClick = {
                            viewModel.onEvent(WordsEvent.ToggleEditMode(-1))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                onClick = {
                                    viewModel.onEvent(WordsEvent.ToggleEditMode(-1))
                                    navController.navigate(Screen.WordCardScreen.route + "?wordId=${word.id}")
                                          },
                                /*onLongClick = {
                                    viewModel.onEvent(WordsEvent.DeleteWord(word))
                                    scope.launch {
                                        val result = scaffoldState.snackbarHostState.showSnackbar(
                                            message = "Dictionary deleted",
                                            actionLabel = "Undo"
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            viewModel.onEvent(WordsEvent.RestoreWord)
                                        }
                                    }
                                }*/
                            )
                    )
                    if (index < state.words.lastIndex)
                        Divider(color = Color.Black, thickness = 1.dp)
                }
            }
        }
    }

}