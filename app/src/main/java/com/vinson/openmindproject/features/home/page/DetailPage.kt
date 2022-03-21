package com.vinson.openmindproject.features.home.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vinson.base.ui.theme.Text10
import com.vinson.openmindproject.features.home.MainViewModel

@Composable
fun DetailPage(
    state: MainViewModel.HomeState,
    action: ((MainViewModel.HomeEvent) -> Unit)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Text10)
            .clickable {
                action(MainViewModel.HomeEvent.ExitDetail)
            }
    ) {
        Text(
            text = "Detail Page"
        )
    }
}