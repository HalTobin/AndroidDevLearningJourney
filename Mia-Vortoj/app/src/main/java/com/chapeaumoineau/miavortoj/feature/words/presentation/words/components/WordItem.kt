package com.chapeaumoineau.miavortoj.feature.words.presentation.words.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.animation.AnimatedVisibility
import com.chapeaumoineau.miavortoj.domain.model.Category
import com.chapeaumoineau.miavortoj.domain.model.Word

@Composable
fun WordItem(modifier: Modifier = Modifier,
             word: Word,
             isFromSource: Boolean,
             wordEdited: Int = -1,
             onMoreClick: () -> Unit,
             onEditClick: () -> Unit,
             onDeleteClick: () -> Unit,
             onBackClick: () -> Unit) {

    Box(modifier = Modifier) {
        Row(modifier = modifier.padding(start = 16.dp).fillMaxSize()) {
            Icon(modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterVertically)
                .size(38.dp),
                imageVector = ImageVector.vectorResource(Category.getCategoryById(word.themeId).icon),
                contentDescription = "")
            Column(modifier = Modifier
                .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)) {
                Text(text = if(isFromSource) word.sourceWord else word.targetWord,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
                Text(text = if(isFromSource) word.targetWord else word.sourceWord,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
            }

        }
        Box(modifier = Modifier
            .align(Alignment.CenterEnd),
            contentAlignment = Alignment.CenterEnd) {

            AnimatedVisibility(
                visible = (wordEdited != word.id),
                enter = fadeIn() + slideInHorizontally(
                    initialOffsetX = { 150 },
                ),
                exit = fadeOut() + slideOutHorizontally(
                    targetOffsetX = { 150 },
                )
            ) {
                IconButton(onClick = onMoreClick) {
                    Icon(modifier = Modifier
                        .fillMaxHeight()
                        .size(24.dp),
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = ""
                    )
                }
            }
            AnimatedVisibility(
                visible = (wordEdited == word.id),
                enter = fadeIn() + slideInHorizontally(
                    initialOffsetX = { 150 },
                ),
                exit = fadeOut() + slideOutHorizontally(
                    targetOffsetX = { 150 },
                )
            ) {
                Row(modifier = Modifier,
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onEditClick) {
                        Icon(imageVector = Icons.Default.Edit,
                            contentDescription = "",
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                        )
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(imageVector = Icons.Default.Delete,
                            contentDescription = "",
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                        )
                    }
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack,
                            contentDescription = "",
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                        )
                    }
                }
            }
        }
    }
}