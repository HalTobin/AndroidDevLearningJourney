package com.chapeaumoineau.miavortoj.feature.quiz.presentation.results

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Percent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.chapeaumoineau.miavortoj.R
import com.chapeaumoineau.miavortoj.feature.quiz.presentation.quiz.QuizEvent
import com.chapeaumoineau.miavortoj.feature.quiz.presentation.quiz.QuizViewModel
import com.chapeaumoineau.miavortoj.presentation.Screen
import com.chapeaumoineau.miavortoj.ui.theme.LightOrange
import com.chapeaumoineau.miavortoj.ui.theme.VeryDarkGray
import kotlinx.coroutines.launch

@Composable
fun ResultsScreen(
    navController: NavController,
    viewModel: ResultsViewModel = hiltViewModel()
) {

    val results = viewModel.results.value

    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) { padding ->

        Column(
            modifier = Modifier
                //.fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.quiz_finished).uppercase(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h2,
                color = LightOrange
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier
                    .border(
                        width = 3.dp,
                        color = VeryDarkGray,
                        shape = RoundedCornerShape(12)
                    )
                    .padding(8.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            modifier = Modifier.size(64.dp),
                            imageVector = Icons.Default.Close,
                            contentDescription = "",
                            tint = Color.Red
                        )
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = results.wrongAnswers.toString(),
                            style = MaterialTheme.typography.h4,
                            color = Color.White
                        )
                    }
                }
                Box(modifier = Modifier
                    .border(
                        width = 3.dp,
                        color = VeryDarkGray,
                        shape = RoundedCornerShape(12)
                    )
                    .padding(8.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            modifier = Modifier.size(64.dp),
                            imageVector = Icons.Default.Done,
                            contentDescription = "",
                            tint = Color.Green
                        )
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = results.correctAnswers.toString(),
                            style = MaterialTheme.typography.h4,
                            color = Color.White
                        )
                    }
                }
                Box(modifier = Modifier
                    .border(
                        width = 3.dp,
                        color = VeryDarkGray,
                        shape = RoundedCornerShape(12)
                    )
                    .padding(8.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            modifier = Modifier.size(64.dp),
                            imageVector = Icons.Default.Percent,
                            contentDescription = "",
                            tint = Color.Yellow
                        )
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = results.getPercent().toString(),
                            style = MaterialTheme.typography.h4,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = results.getEmote(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h1,
                color = LightOrange
            )

            Spacer(modifier = Modifier.height(16.dp))

        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(6.dp)),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.dark_green)),
                onClick = {
                    viewModel.viewModelScope.launch {
                        navController.navigateUp()
                        navController.navigateUp()
                    }
                },
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = 8.dp,
                    end = 20.dp,
                    bottom = 8.dp
                )
            ) {
                Text(text = stringResource(R.string.quiz_validate_continue), style = MaterialTheme.typography.h6)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

    }
}