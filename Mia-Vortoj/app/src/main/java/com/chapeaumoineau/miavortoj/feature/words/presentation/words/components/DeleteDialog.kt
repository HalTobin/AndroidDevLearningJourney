package com.chapeaumoineau.miavortoj.feature.words.presentation.words.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.chapeaumoineau.miavortoj.R
import com.chapeaumoineau.miavortoj.domain.model.Word
import com.chapeaumoineau.miavortoj.feature.words.presentation.words.WordsEvent
import com.chapeaumoineau.miavortoj.feature.words.presentation.words.WordsViewModel

@Composable
fun DeleteDialog(isVisible: Boolean,
                 word: Word,
                 viewModel: WordsViewModel = hiltViewModel()) {
    if(isVisible) Dialog(onDismissRequest = { viewModel.onEvent(WordsEvent.DismissDeleteDialog) }){
        Surface(
            modifier = Modifier,
            shape = RoundedCornerShape(8.dp),
            contentColor = MaterialTheme.colors.primary
        ) {
            Column(modifier = Modifier
                .background(Color.DarkGray)) {

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = stringResource(R.string.dialog_warning),
                        style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = stringResource(R.string.word_screen_delete_text) + " "
                            + word.sourceWord,
                        style = MaterialTheme.typography.subtitle1)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Button(modifier = Modifier,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary),
                            onClick = {
                                viewModel.onEvent(WordsEvent.DismissDeleteDialog)
                            },
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                top = 8.dp,
                                end = 18.dp,
                                bottom = 8.dp
                            )
                        ) {
                            Text(text = stringResource(R.string.dialog_cancel),
                                style = MaterialTheme.typography.subtitle1,
                                color = Color.Black)
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(modifier = Modifier,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Red),
                            onClick = {
                                viewModel.onEvent(WordsEvent.DeleteWord(word))
                            },
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                top = 8.dp,
                                end = 18.dp,
                                bottom = 8.dp
                            )
                        ) {
                            Text(text = stringResource(R.string.dialog_confirm),
                                style = MaterialTheme.typography.subtitle1,
                                color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}
