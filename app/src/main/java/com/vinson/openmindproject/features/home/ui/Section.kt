package com.vinson.openmindproject.features.home.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridItemSpan
import androidx.compose.foundation.lazy.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.LazyGridScope
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.vinson.base.ui.theme.BoldBody
import com.vinson.base.ui.theme.Primary100
import com.vinson.base.ui.theme.Shapes
import com.vinson.base.ui.util.rememberPicturePainter
import com.vinson.datamodel.asset.Asset
import com.vinson.datamodel.asset.Contract

@OptIn(ExperimentalFoundationApi::class)
fun LazyGridScope.stateUi(
    results: LazyPagingItems<Asset>,
    span: (LazyGridItemSpanScope.() -> GridItemSpan),
) {
    with(results.loadState) {
        when {
            refresh is LoadState.Loading ||
                    append is LoadState.Loading -> {
                item(span) {
                    LoadingView(modifier = Modifier.fillMaxWidth())
                }
            }
            refresh is LoadState.Error -> {
                val e = refresh as LoadState.Error
                item(span) {
                    ErrorItem(
                        message = e.error.localizedMessage!!,
                        modifier = Modifier.fillMaxWidth(),
                        onClickRetry = { results.retry() }
                    )
                }
            }
            append is LoadState.Error -> {
                val e = append as LoadState.Error
                item(span) {
                    ErrorItem(
                        message = e.error.localizedMessage!!,
                        modifier = Modifier.fillMaxWidth(),
                        onClickRetry = { results.retry() }
                    )
                }
            }
        }
    }
}

@Composable
fun AssetCard(
    asset: Asset,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, Shapes.medium)
            .clickable { onClick() }
    ) {
        Image(
            painter = rememberPicturePainter(asset.image),
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .size(150.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = asset.name ?: "",
            style = BoldBody,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun LoadingView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = Primary100)
    }
}

@Composable
fun LoadingItem() {
    CircularProgressIndicator(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
fun ErrorItem(
    message: String,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
) {
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            maxLines = 1,
            modifier = Modifier.weight(1f),
            style = BoldBody,
            color = Color.Red
        )
        OutlinedButton(onClick = onClickRetry) {
            Text(text = "Try again")
        }
    }
}

@Preview
@Composable
fun AssetCardPreview() {
    AssetCard(
        asset = Asset(
            331081405,
            "https://lh3.googleusercontent.com/1bRgXZeCvQOxgEshviiPLNaPkWTgHpSohi5fjvsKKR69mYqM2W3YarziRlE6KouoiYMFCnjb-K4ZnFrNAzD8gaKcF-vjfZSftUuhkg",
            "ReneVerse Golden Pass",
            Contract("0xca063e5e22ddcb8b7a8f4ccb9d3da4107be9dce6"),
            "12",
        ),
        onClick = {}
    )
}