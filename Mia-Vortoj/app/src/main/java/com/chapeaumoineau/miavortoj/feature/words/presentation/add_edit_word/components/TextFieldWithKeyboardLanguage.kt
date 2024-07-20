package com.chapeaumoineau.miavortoj.feature.words.presentation.add_edit_word.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun TextFieldWithKeyboardLanguage(text: String,
                             hint: String,
                             modifier: Modifier = Modifier,
                             isHintVisible: Boolean = true,
                             onValueChange: (String) -> Unit,
                             textStyle: TextStyle = TextStyle(),
                             keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
                                 capitalization = KeyboardCapitalization.Sentences,
                                 autoCorrect = false,
                                 keyboardType = KeyboardType.Text,
                                 imeAction = ImeAction.Next
                             ),
                             singleLine: Boolean = false,
                             onFocusChange: (FocusState) -> Unit) {

    Box(modifier = modifier) {
        BasicTextField(value = text,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            modifier = Modifier.fillMaxWidth().onFocusChanged { onFocusChange(it) })
        if(isHintVisible) Text(text = hint, style = textStyle, color = Color.DarkGray)
    }

}