package com.chapeaumoineau.miavortoj.feature.words.presentation.word_card

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chapeaumoineau.miavortoj.R
import com.chapeaumoineau.miavortoj.presentation.Screen

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WordCardScreen(navController: NavController,
                    viewModel: WordCardViewModel = hiltViewModel()) {

    val word = viewModel.word.value
    val category = viewModel.category.value
    val language = viewModel.language.value

    val speech = viewModel.speech.value

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {
            navController.navigate(Screen.AddEditWordScreen.route + "?dictionaryId=${word.dictionaryId}&wordId=${word.id}")
        }, backgroundColor = MaterialTheme.colors.primary) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit word")
        }
    }, scaffoldState = scaffoldState) {
        Column(modifier = Modifier.fillMaxSize().background(language.getDarkColor()).padding(16.dp)) {
            Row() {
                Icon(modifier = Modifier.size(60.dp).padding(end = 16.dp).align(Alignment.CenterVertically),
                    imageVector = ImageVector.vectorResource(category.icon),
                    contentDescription = "")
                Column() {
                    Text(text = word.targetWord, style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold, color = MaterialTheme.colors.primary)
                    Text(text = word.sourceWord, style = MaterialTheme.typography.h5, color = MaterialTheme.colors.primary)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = word.notes, style = MaterialTheme.typography.subtitle1, color = MaterialTheme.colors.primary)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally), horizontalArrangement = Arrangement.Center) {
                Text(text = word.emote, style = MaterialTheme.typography.h1, color = MaterialTheme.colors.primary)
            }

            Spacer(modifier = Modifier.height(128.dp))

            Button(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(32.dp)),
                colors = ButtonDefaults.buttonColors(backgroundColor = if(speech) colorResource(R.color.dark_green) else colorResource(R.color.dark_gray)),
                onClick = {
                    viewModel.onEvent(WordCardEvent.PlayWord)
                },
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 8.dp,
                    end = 18.dp,
                    bottom = 8.dp
                )
            ) {
                Icon(
                    Icons.Default.VolumeUp,
                    contentDescription = "Text to speech",
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = stringResource(R.string.word_card_play), style = MaterialTheme.typography.h5)
            }

        }
    }

}