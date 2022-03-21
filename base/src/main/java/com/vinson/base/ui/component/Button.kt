package com.vinson.base.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.vinson.base.ui.theme.BoldBody
import com.vinson.base.ui.theme.Primary100
import com.vinson.base.ui.theme.Text20

@Composable
fun DarkNormalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    NormalButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        colors = buttonColors(
            backgroundColor = Primary100,
            disabledBackgroundColor = Text20
        ),
        textColor = Color.White,
        enabled = enabled
    )
}

@Composable
fun LightNormalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    NormalButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        colors = buttonColors(
            backgroundColor = Color.White,
            disabledBackgroundColor = Text20
        ),
        textColor = Primary100,
        enabled = enabled
    )
}

@Composable
fun NormalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors,
    textColor: Color,
    enabled: Boolean
) {
    BaseButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        colors = colors,
        style = BoldBody + TextStyle(color = textColor),
        paddingValues = PaddingValues(32.dp, 16.dp),
        enabled = enabled
    )
}

@Composable
fun BaseButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors,
    style: TextStyle,
    paddingValues: PaddingValues,
    enabled: Boolean
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        contentPadding = paddingValues
    ) {
        Text(
            text = text,
            style = style
        )
    }
}