package com.chapeaumoineau.miavortoj.feature.words.presentation.words.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chapeaumoineau.miavortoj.R
import com.chapeaumoineau.miavortoj.util.OrderType
import com.chapeaumoineau.miavortoj.feature.words.util.WordOrder
import com.chapeaumoineau.miavortoj.feature.dictionary.presentation.dictionnaries.components.DefaultRadioButton

@Composable
fun OrderWordsSection(modifier: Modifier = Modifier,
                      wordOrder: WordOrder = WordOrder.Source(OrderType.Ascending),
                      onOrderChange: (WordOrder) -> Unit) {
    Column(modifier = modifier) {
        Row(modifier = modifier.fillMaxWidth()) {
            DefaultRadioButton(text = stringResource(id = R.string.order_word_source),
                selected = wordOrder is WordOrder.Source,
                onSelect = { onOrderChange(WordOrder.Source(wordOrder.orderType)) })
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(text = stringResource(id = R.string.order_word_target),
                selected = wordOrder is WordOrder.Target,
                onSelect = { onOrderChange(WordOrder.Target(wordOrder.orderType)) })
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(text = stringResource(id = R.string.order_word_theme),
                selected = wordOrder is WordOrder.Theme,
                onSelect = { onOrderChange(WordOrder.Theme(wordOrder.orderType)) })
            Spacer(modifier = Modifier.width(16.dp))
        }
        Row(modifier = modifier.fillMaxWidth()) {
            DefaultRadioButton(text = stringResource(id = R.string.order_ascending),
                selected = wordOrder.orderType is OrderType.Ascending,
                onSelect = { onOrderChange(wordOrder.copy(OrderType.Ascending)) })
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(text = stringResource(id = R.string.order_descending),
                selected = wordOrder.orderType is OrderType.Descending,
                onSelect = { onOrderChange(wordOrder.copy(OrderType.Descending)) })
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}