package com.chapeaumoineau.miavortoj.feature.quiz.presentation.quiz

import android.annotation.SuppressLint
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chapeaumoineau.miavortoj.R
import com.chapeaumoineau.miavortoj.presentation.Screen
import com.chapeaumoineau.miavortoj.ui.theme.DarkGreen
import com.chapeaumoineau.miavortoj.ui.theme.DarkOrange
import com.chapeaumoineau.miavortoj.ui.theme.DarkRed
import com.chapeaumoineau.miavortoj.ui.theme.Transparent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun QuizScreen(navController: NavController,
               viewModel: QuizViewModel = hiltViewModel()) {

    val answer = viewModel.answer.value
    val category = viewModel.category.value
    val userEntry = viewModel.userEntry.value
    val language = viewModel.language.value
    val progress = viewModel.progress.value
    val isTtsAvailable = viewModel.isTtsAvailable.value

    var textFieldColor = Transparent
    val buttonNextColor = DarkRed
    val buttonValidateColor = DarkGreen

    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    val scope = rememberCoroutineScope()

    val textFieldBackgroundAnimated = remember {
        Animatable(textFieldColor)
    }

    val buttonNextBackgroundAnimated = remember {
        Animatable(buttonNextColor)
    }

    val buttonValidateBackgroundAnimated = remember {
        Animatable(buttonValidateColor)
    }
    
    fun animateTextFieldBackgroundColorTo(color: Color) {
        scope.launch {
            textFieldBackgroundAnimated.animateTo(
                targetValue = color,
                animationSpec = tween(durationMillis = 250),
            )
            textFieldColor = Transparent
            textFieldBackgroundAnimated.animateTo(
                targetValue = textFieldColor,
                animationSpec = tween(durationMillis = 500),
            )
        }
    }
    
    fun animateNextAndValidateButtonsColors(on: Boolean) {
        scope.launch { 
            if (on) {
                buttonNextBackgroundAnimated.animateTo(
                    targetValue = DarkRed,
                    animationSpec = tween(durationMillis = 200)
                )
            } else {
                buttonNextBackgroundAnimated.animateTo(
                    targetValue = Color.LightGray,
                    animationSpec = tween(durationMillis = 200)
                )
            }
        }
        scope.launch {
            if (on) {
                buttonValidateBackgroundAnimated.animateTo(
                    targetValue = DarkGreen,
                    animationSpec = tween(durationMillis = 200)
                )
            } else {
                buttonValidateBackgroundAnimated.animateTo(
                    targetValue = Color.LightGray,
                    animationSpec = tween(durationMillis = 200)
                )
            }
        }
    }

    fun validateAnswer() {
        if (sheetState.isCollapsed) scope.launch {
            viewModel.onEvent(QuizEvent.CheckAnswer)
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is QuizViewModel.UiEvent.CloseQuiz -> navController.navigateUp()
                is QuizViewModel.UiEvent.AnswerValid -> animateTextFieldBackgroundColorTo(DarkGreen)
                is QuizViewModel.UiEvent.AnswerClose -> animateTextFieldBackgroundColorTo(DarkOrange)
                is QuizViewModel.UiEvent.AnswerWrong -> animateTextFieldBackgroundColorTo(DarkRed)
                is QuizViewModel.UiEvent.OpenResults -> {
                    navController.navigate(
                        Screen.ResultsScreen.route
                                + "?nbQuestions=${event.results.duration}"
                                + "&nbErrors=${event.results.wrongAnswers}"
                                + "&nbCorrects=${event.results.correctAnswers}"
                    )
                }
            }
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetGesturesEnabled = false,
        sheetShape = MaterialTheme.shapes.small,
        sheetBackgroundColor = Color.DarkGray,
        sheetContent = {

        //ResultDialog(isVisible = resultDialogState)

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.quiz_answer_was),
                style = MaterialTheme.typography.subtitle1,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = answer.answer,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp)),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.dark_green)),
                onClick = {
                    scope.launch {
                        sheetState.collapse()
                        animateNextAndValidateButtonsColors(true)
                        viewModel.onEvent(QuizEvent.NextWord)
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
        }
    }) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(language.getDarkColor())
            .padding(16.dp)) {

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                LinearProgressIndicator(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp),
                    progress = progress)
            }

            Spacer(modifier = Modifier.height(64.dp))

            Row(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally), horizontalArrangement = Arrangement.Center) {
                Text(text = answer.emote, style = MaterialTheme.typography.h1)
            }

            Spacer(modifier = Modifier.height(64.dp))

            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth(0.75f),
                    text = answer.question,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    maxLines = 4)
                Spacer(modifier = Modifier.width(16.dp))
                if(isTtsAvailable && answer.isFromTarget) {
                    Button(modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clip(RoundedCornerShape(8.dp)),
                        colors = ButtonDefaults.buttonColors(backgroundColor = language.getLightColor()),
                        onClick = {
                            viewModel.onEvent(QuizEvent.SpeakWord)
                        }
                    ) {
                        Icon(
                            Icons.Default.VolumeUp,
                            contentDescription = "Text to speech",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Bottom) {
                TextField(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 32.dp),
                    value = userEntry,
                    singleLine = true,
                    onValueChange = {
                        viewModel.onEvent(QuizEvent.EnteredEntry(it))
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            validateAnswer()
                        },
                        onPrevious = {
                            //sheetState.
                        }
                    ),
                    label = {
                        Text(text = stringResource(R.string.quiz_enter_response), style = MaterialTheme.typography.h6, color = Color.LightGray)
                    },
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = textFieldBackgroundAnimated.value),
                    textStyle = MaterialTheme.typography.h6,
                    maxLines = 1,
                    readOnly = sheetState.isExpanded
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 32.dp)) {

                    Button(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp)),
                        enabled = sheetState.isCollapsed,
                        colors =
                        if (sheetState.isCollapsed) ButtonDefaults.buttonColors(backgroundColor = buttonNextBackgroundAnimated.value)
                        else ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                        onClick = {
                            if (sheetState.isCollapsed) scope.launch {
                                sheetState.expand()
                                animateNextAndValidateButtonsColors(false)
                            }
                        },
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            top = 8.dp,
                            end = 20.dp,
                            bottom = 8.dp
                        )
                    ) {
                        Text(text = stringResource(R.string.quiz_ask_answer), style = MaterialTheme.typography.h6)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp)),
                        enabled = sheetState.isCollapsed,
                        colors =
                        if (sheetState.isCollapsed) ButtonDefaults.buttonColors(backgroundColor = buttonValidateBackgroundAnimated.value)
                        else ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                        onClick = { validateAnswer() },
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            top = 8.dp,
                            end = 20.dp,
                            bottom = 8.dp
                        )
                    ) {
                        Text(text = stringResource(R.string.quiz_validate_answer), style = MaterialTheme.typography.h6)
                    }

                }
            }

        }

    }

}