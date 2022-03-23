package com.vinson.openmindproject.features.home.page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.vinson.base.ui.theme.BoldTitle
import com.vinson.base.ui.theme.Text10
import com.vinson.openmindproject.features.home.MainViewModel
import com.vinson.openmindproject.features.home.ui.AssetCard
import com.vinson.openmindproject.features.home.ui.stateUi

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainPage(
    state: MainViewModel.HomeState,
    action: ((MainViewModel.HomeEvent) -> Unit)
) {
    val scrollState = rememberLazyListState()
    val results = state.assets.collectAsLazyPagingItems()
    val span: (LazyGridItemSpanScope.() -> GridItemSpan) = { GridItemSpan(2) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Text10)
    ) {
        Text(
            text = "Balance : ${state.balance}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            style = BoldTitle,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        LazyVerticalGrid(
            state = scrollState,
            cells = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                start = 4.dp,
                top = 16.dp,
                end = 4.dp,
                bottom = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(results.itemCount) {
                val asset = results[it] ?: return@items
                AssetCard(asset) {
                    action(MainViewModel.HomeEvent.OpenDetail(asset))
                }
            }
            stateUi(results = results, span = span)
        }
    }
}