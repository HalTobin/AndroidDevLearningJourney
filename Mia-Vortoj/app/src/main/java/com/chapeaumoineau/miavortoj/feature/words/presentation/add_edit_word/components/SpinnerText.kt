package com.chapeaumoineau.miavortoj.feature.words.presentation.add_edit_word.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.chapeaumoineau.miavortoj.R
import com.chapeaumoineau.miavortoj.domain.model.Category
import com.chapeaumoineau.miavortoj.feature.words.presentation.add_edit_word.AddEditWordEvent
import com.chapeaumoineau.miavortoj.feature.words.presentation.add_edit_word.AddEditWordViewModel

@Composable
fun SpinnerText(
    modifier: Modifier,
    viewModel: AddEditWordViewModel,
    items: List<Category>,
    itemSelected: Category
)  {

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable { expanded = !expanded },
        contentAlignment = Alignment.Center
    ) {
        Row(modifier = Modifier
            .padding(24.dp)
            .padding(8.dp)
            .fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .fillMaxSize()
                    .clickable { expanded = !expanded },
                readOnly = true,
                value = stringResource(id = itemSelected.text),
                singleLine = true,
                textStyle = MaterialTheme.typography.subtitle1,
                onValueChange = {  },
                label = {
                    Text(
                        text = stringResource(id = R.string.add_edit_word_category_hint),
                        style = MaterialTheme.typography.subtitle1,
                        color = Color.LightGray
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = ""
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        items.forEach { item ->
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
            )
        }
    }

}