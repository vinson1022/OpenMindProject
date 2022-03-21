package com.vinson.base.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vinson.base.ui.theme.BoldBody
import com.vinson.base.ui.util.rememberAvatarPainter

@Composable
fun UserRow(
    name: String,
    avatar: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAvatarPainter(url = avatar),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = name,
            modifier = Modifier.padding(4.dp),
            style = BoldBody,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}