package com.chapeaumoineau.miavortoj.feature.words.presentation.add_edit_word.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.chapeaumoineau.miavortoj.domain.model.Category

@Composable
fun CategoryDialogItem(category: Category,
                       modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Column(modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center)
            .padding(start = 16.dp)) {
            Text(text = stringResource(id = category.text),
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.primary
            )
        }

        Column(modifier = Modifier
            .padding(end = 16.dp, bottom = 4.dp, top = 4.dp, start = 8.dp)
            .align(Alignment.CenterEnd)) {
            Icon(imageVector = ImageVector.vectorResource(category.icon),
                contentDescription = "",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
        }
    }

}