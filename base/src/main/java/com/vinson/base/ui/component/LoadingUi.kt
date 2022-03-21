package com.vinson.base.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vinson.base.ui.theme.Primary100
import com.vinson.base.ui.theme.Text100Alpha60


@Preview
@Composable
fun LoadingContent() {
    Box(
            modifier = Modifier
                    .fillMaxSize()
                    .background(Text100Alpha60)
                    .clickable {
                        // Do Nothing
                    }
    ) {
        CircularProgressIndicator(
                modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center),
                color = Primary100,
                strokeWidth = 8.dp
        )
    }
}