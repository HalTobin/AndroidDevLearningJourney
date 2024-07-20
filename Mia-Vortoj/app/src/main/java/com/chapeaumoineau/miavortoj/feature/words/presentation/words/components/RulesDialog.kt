package com.chapeaumoineau.miavortoj.feature.words.presentation.words.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.chapeaumoineau.miavortoj.R
import com.chapeaumoineau.miavortoj.feature.quiz.model.Rules
import com.chapeaumoineau.miavortoj.feature.words.presentation.words.WordsEvent
import com.chapeaumoineau.miavortoj.feature.words.presentation.words.WordsViewModel

@Composable
fun RulesDialog(isVisible: Boolean,
                rules: Rules,
                viewModel: WordsViewModel = hiltViewModel()) {

    if(isVisible) Dialog(onDismissRequest = { viewModel.onEvent(WordsEvent.DismissRulesDialog) }) {
        Surface(modifier = Modifier, shape = RoundedCornerShape(8.dp), contentColor = MaterialTheme.colors.primary) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                Button(modifier = Modifier
                    .clip(RoundedCornerShape(32.dp))
                    /*.border(
                        width = 4.dp,
                        color = if (viewModel.difficulty.value == 0) {
                            Color.Black
                        } else Color.Transparent,
                        shape = CircleShape
                    )*/,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if(viewModel.rules.value.difficulty == Rules.DIFFICULTY_EASY) colorResource(R.color.difficulty_easy_on)
                        else colorResource(R.color.difficulty_easy_off)
                    ),
                    onClick = {
                        //viewModel.onEvent(AddEditWordEvent.ChangeDifficulty(0))
                    },
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        top = 8.dp,
                        end = 18.dp,
                        bottom = 8.dp
                    )
                ) {
                    Text(text = stringResource(R.string.difficulty_easy),
                        style = MaterialTheme.typography.subtitle1,
                        color = Color.Black)
                }
                Spacer(modifier = Modifier.width(8.dp))

                Button(modifier = Modifier
                    .clip(RoundedCornerShape(32.dp)),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if(viewModel.rules.value.difficulty == Rules.DIFFICULTY_MEDIUM) colorResource(R.color.difficulty_medium_on)
                        else colorResource(R.color.difficulty_medium_off)
                    ),
                    onClick = {
                        //viewModel.onEvent(AddEditWordEvent.ChangeDifficulty(1))
                    },
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        top = 8.dp,
                        end = 18.dp,
                        bottom = 8.dp
                    )
                ) {
                    Text(text = stringResource(R.string.difficulty_medium),
                        style = MaterialTheme.typography.subtitle1,
                        color = Color.Black)
                }
                Spacer(modifier = Modifier.width(8.dp))

                Button(modifier = Modifier
                    .clip(RoundedCornerShape(32.dp)),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if(viewModel.rules.value.difficulty == Rules.DIFFICULTY_HARD) colorResource(R.color.difficulty_hard_on)
                        else colorResource(R.color.difficulty_hard_off)
                    ),
                    onClick = {
                        //viewModel.onEvent(AddEditWordEvent.ChangeDifficulty(2))
                    },
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        top = 8.dp,
                        end = 18.dp,
                        bottom = 8.dp
                    )
                ) {
                    Text(text = stringResource(R.string.difficulty_hard),
                        style = MaterialTheme.typography.subtitle1,
                        color = Color.Black)
                }
                Button(modifier = Modifier
                    .clip(RoundedCornerShape(32.dp)),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if(viewModel.rules.value.difficulty == Rules.DIFFICULTY_ALL) Color.White
                        else Color.LightGray
                    ),
                    onClick = {
                        //viewModel.onEvent(AddEditWordEvent.ChangeDifficulty(2))
                    },
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        top = 8.dp,
                        end = 18.dp,
                        bottom = 8.dp
                    )
                ) {
                    Text(text = stringResource(R.string.difficulty_hard),
                        style = MaterialTheme.typography.subtitle1,
                        color = Color.Black)
                }
            }
        }
    }

}