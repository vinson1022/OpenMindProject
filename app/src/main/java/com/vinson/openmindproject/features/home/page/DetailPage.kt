package com.vinson.openmindproject.features.home.page

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vinson.base.R
import com.vinson.base.ui.component.CollapsingToolbar
import com.vinson.base.ui.component.DarkNormalButton
import com.vinson.base.ui.theme.Body
import com.vinson.base.ui.theme.BoldBody
import com.vinson.base.ui.theme.Text10
import com.vinson.base.ui.util.openLink
import com.vinson.base.ui.util.rememberPictureAutoScalePainter
import com.vinson.openmindproject.App
import com.vinson.openmindproject.features.home.MainViewModel

@Composable
fun DetailPage(
    state: MainViewModel.HomeState,
    action: ((MainViewModel.HomeEvent) -> Unit)
) {
    val detail = state.assetDetail
    
    CollapsingToolbar(
        title = detail?.collection?.name ?: "",
        leaderPainter = painterResource(id = R.drawable.ic_back),
        leaderPainterClick = { action(MainViewModel.HomeEvent.ExitDetail) },
        modifier = Modifier
            .fillMaxSize()
            .background(Text10)
    ) {
        Box(
            modifier = Modifier
            .fillMaxSize()
            .background(Text10)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp, 0.dp, 0.dp, 96.dp)
                    .background(Text10)
                    .align(Alignment.TopCenter)
                    .verticalScroll(rememberScrollState())
            ) {
                Image(
                    painter = rememberPictureAutoScalePainter(detail?.image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    text = detail?.name ?: "",
                    style = BoldBody,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = detail?.description?: "",
                    style = Body,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            DarkNormalButton(
                text = "Permalink",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                onClick = { openLink(App.getInstance(), detail?.link ?: "") },
                enabled = !detail?.link.isNullOrEmpty()
            )
        }
    }
}