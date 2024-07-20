package com.chapeaumoineau.miavortoj.feature.dictionary.presentation.dictionnaries.components

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.chapeaumoineau.miavortoj.domain.model.Dictionary
import com.chapeaumoineau.miavortoj.domain.model.Language

@Composable
fun DictionaryItem(modifier: Modifier = Modifier,
                   dictionary: Dictionary,
                   dictionaryEdited: Int = -1,
                   onMoreClick: () -> Unit,
                   onDeleteClick: () -> Unit,
                   onEditClick:() -> Unit,
                   onBackClick:() -> Unit,
                   cornerRadius: Dp = 10.dp) {

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val clipPath = Path().apply {
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                lineTo(0f, 0f)
                close()
            }

            clipPath(clipPath) {
                drawRoundRect(
                    color = Language.getLanguageByIso(dictionary.languageIso).getLightColor(),
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
        }

        Row(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(Language.getLanguageByIso(dictionary.languageIso).flag),
                contentDescription = "",
                modifier = Modifier
                    .size(58.dp)
                    .shadow(15.dp, CircleShape)
                    .clip(CircleShape)
                    .border(
                        width = 3.dp,
                        color = Color.Black,
                        shape = CircleShape
                    )
            )
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                Text(text = dictionary.title,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = dictionary.description,
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Box(modifier = Modifier
                .padding(end = 16.dp)
                .align(Alignment.CenterEnd)
                .background(Language.getLanguageByIso(dictionary.languageIso).getLightColor()),
            contentAlignment = Alignment.CenterEnd,
        ) {

            AnimatedVisibility(
                visible = (dictionaryEdited != dictionary.id),
                enter = fadeIn() + slideInHorizontally(
                    initialOffsetX = { 150 },
                ),
                exit = fadeOut() + slideOutHorizontally(
                    targetOffsetX = { 150 },
                )
            ) {
                IconButton(onClick = onMoreClick) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "",
                        tint = MaterialTheme.colors.secondary,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                    )
                }
            }
            AnimatedVisibility(
                visible = (dictionaryEdited == dictionary.id),
                enter = fadeIn() + slideInHorizontally(
                    initialOffsetX = { 150 },
                ),
                exit = fadeOut() + slideOutHorizontally(
                    targetOffsetX = { 150 },
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "",
                            tint = MaterialTheme.colors.secondary,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "",
                            tint = MaterialTheme.colors.secondary,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                    }
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "",
                            tint = MaterialTheme.colors.secondary,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                    }
                }
            }
        }
    }
}