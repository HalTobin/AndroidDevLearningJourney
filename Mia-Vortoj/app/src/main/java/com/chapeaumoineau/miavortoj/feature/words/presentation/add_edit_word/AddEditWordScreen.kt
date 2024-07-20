package com.chapeaumoineau.miavortoj.feature.words.presentation.add_edit_word

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chapeaumoineau.miavortoj.R
import com.chapeaumoineau.miavortoj.feature.words.presentation.add_edit_word.components.CategoryDialog
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditWordScreen(navController: NavController,
                      viewModel: AddEditWordViewModel = hiltViewModel()) {
    val sourceState = viewModel.source.value
    val targetState = viewModel.target.value
    val notesState = viewModel.notes.value
    val emoteState = viewModel.emote.value
    val colorState = viewModel.color.value

    val targetHintState = viewModel.targetHint.value

    val dialogState = viewModel.dialog.value
    val categoryListState = viewModel.categoriesWithTranslation.value
    val categoryState = viewModel.category.value

    val scaffoldState = rememberScaffoldState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is AddEditWordViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = event.message)
                }
                is AddEditWordViewModel.UiEvent.SaveWord -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(floatingActionButton = {
        CategoryDialog(isVisible = dialogState)
        FloatingActionButton(onClick = { viewModel.onEvent(AddEditWordEvent.SaveWord) }, backgroundColor = MaterialTheme.colors.primary) {
            Icon(imageVector = Icons.Default.Save, contentDescription = "Save dictionary")
        }
    },
        scaffoldState = scaffoldState) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(colorState)
            .padding(16.dp)) {

            Spacer(modifier = Modifier.height(16.dp))

            TextField(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
                value = sourceState,
                onValueChange = {
                    viewModel.onEvent(AddEditWordEvent.EnteredSource(it))
                },
                label = {
                    Text(text = stringResource(R.string.add_edit_word_source_hint), style = MaterialTheme.typography.h6, color = Color.LightGray)
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.h5,
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
                value = targetState,
                onValueChange = {
                    viewModel.onEvent(AddEditWordEvent.EnteredTarget(it))
                },
                label = {
                    Text(text = targetHintState, style = MaterialTheme.typography.h6, color = Color.LightGray)
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.h5,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(Modifier, Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                TextField(modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                    value = emoteState,
                    onValueChange = {
                        viewModel.onEvent(AddEditWordEvent.EnteredEmote(it))
                    },
                    label = {
                        Text(text = stringResource(R.string.add_edit_word_emote_hint), style = MaterialTheme.typography.subtitle1, color = Color.LightGray)
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.h6,
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp)
                        .background(Color(0x14000000))
                        .clickable { expanded = !expanded }
                ) {
                    Column(modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp)) {
                        Text(
                            text = stringResource(id = R.string.add_edit_word_category_hint),
                            style = MaterialTheme.typography.subtitle1
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = stringResource(id = categoryState.text),
                            maxLines = 1,
                            style = MaterialTheme.typography.h6,
                            color = Color.LightGray
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            categoryListState.forEach { item ->
                                DropdownMenuItem(onClick = {
                                    viewModel.onEvent(AddEditWordEvent.OnCategorySelected(item.id))
                                    expanded = false
                                }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(item.icon),
                                        contentDescription = ""
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = stringResource(id = item.text))
                                }
                            }
                        }
                    }
                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 8.dp),
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = ""
                    )
                    Spacer(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0x33000000))
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp)
                .weight(1f),
                value = notesState,
                onValueChange = {
                    viewModel.onEvent(AddEditWordEvent.EnteredDescription(it))
                },
                label = {
                    Text(text = stringResource(R.string.add_edit_word_notes_hint), style = MaterialTheme.typography.subtitle1, color = Color.LightGray)
                },
                singleLine = false,
                textStyle = MaterialTheme.typography.subtitle1,
            )

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}