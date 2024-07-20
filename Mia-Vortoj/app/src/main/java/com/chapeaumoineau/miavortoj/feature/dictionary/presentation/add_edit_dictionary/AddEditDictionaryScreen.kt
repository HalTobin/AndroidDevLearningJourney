package com.chapeaumoineau.miavortoj.feature.dictionary.presentation.add_edit_dictionary

import android.annotation.SuppressLint
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chapeaumoineau.miavortoj.R
import com.chapeaumoineau.miavortoj.feature.dictionary.presentation.add_edit_dictionary.components.LanguageDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditDictionaryScreen(navController: NavController,
                            viewModel: AddEditDictionaryViewModel = hiltViewModel()) {
    val titleState = viewModel.title.value
    val descriptionState = viewModel.description.value
    val languageSelection = viewModel.dictionaryLanguage.value
    val languagesState = viewModel.languages.value
    val dialogState = viewModel.dialog.value

    val languageListState = viewModel.languageList.value

    val scaffoldState = rememberScaffoldState()

    val dictionaryBackgroundAnimated = remember {
        Animatable(languageSelection.getDarkColor())
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is AddEditDictionaryViewModel.UiEvent.ChangeLanguage -> {
                    dictionaryBackgroundAnimated.animateTo(
                        targetValue = event.language.getDarkColor(),
                        animationSpec = tween(durationMillis = 500)
                    )
                }
                is AddEditDictionaryViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = event.message)
                }
                is AddEditDictionaryViewModel.UiEvent.SaveDictionary -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(floatingActionButton = {
        LanguageDialog(isVisible = dialogState)
        FloatingActionButton(onClick = { viewModel.onEvent(AddEditDictionaryEvent.SaveDictionary) }, backgroundColor = MaterialTheme.colors.primary) {
            Icon(imageVector = Icons.Default.Save, contentDescription = "Save dictionary")
        }
    },
        scaffoldState = scaffoldState) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(dictionaryBackgroundAnimated.value)
            .padding(16.dp)) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {

                for (c in 0..4) {
                    if(c!=4) {
                        val myLanguage = languagesState[c]
                        Image(painter = painterResource(myLanguage.flag),
                            contentDescription = "",
                            modifier = Modifier
                                .size(50.dp)
                                .shadow(15.dp, CircleShape)
                                .clip(CircleShape)
                                .border(
                                    width = 3.dp,
                                    color = if (viewModel.dictionaryLanguage.value.iso == myLanguage.iso) {
                                        Color.Black
                                    } else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable {
                                    scope.launch {

                                    }
                                    if (c != 4) {
                                        viewModel.onEvent(
                                            AddEditDictionaryEvent.ChangeLanguage(myLanguage.iso)
                                        )
                                    } else {
                                        viewModel.onEvent(AddEditDictionaryEvent.MoreLanguage)
                                    }
                                }
                        )
                    }
                    else Icon(imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(15.dp, CircleShape)
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.primary)
                            .clickable {
                                viewModel.onEvent(AddEditDictionaryEvent.MoreLanguage)
                            }
                    )
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(modifier = Modifier.align(Alignment.CenterHorizontally),
                text = languageSelection.name,
                style = MaterialTheme.typography.h4)

            Spacer(modifier = Modifier.height(16.dp))

            TextField(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
                value = titleState,
                onValueChange = {
                    viewModel.onEvent(AddEditDictionaryEvent.EnteredTitle(it))
                },
                label = {
                    Text(text = stringResource(R.string.add_edit_dictionary_screen_title_hint), style = MaterialTheme.typography.h5, color = Color.LightGray)
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.h4,
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp)
                .weight(1f),
                value = descriptionState,
                onValueChange = {
                    viewModel.onEvent(AddEditDictionaryEvent.EnteredDescription(it))
                },
                label = {
                    Text(text = stringResource(R.string.add_edit_dictionary_screen_description_hint), style = MaterialTheme.typography.subtitle1, color = Color.LightGray)
                },
                maxLines = 2,
                textStyle = MaterialTheme.typography.h6,
            )
        }
    }
}