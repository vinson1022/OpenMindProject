package com.vinson.base.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.size.Scale
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.vinson.base.R

val rounded by lazy { RoundedCornersTransformation(8.dp.value) }
val circle by lazy { CircleCropTransformation() }

@OptIn(ExperimentalCoilApi::class)
@Composable
fun rememberPicturePainter(url: String?) = rememberImagePainter(
        data = url,
        builder = {
            scale(Scale.FIT)
            transformations(rounded)
            placeholder(R.mipmap.ic_picture_default)
            error(R.mipmap.ic_picture_default)
        }
)

@OptIn(ExperimentalCoilApi::class)
@Composable
fun rememberAvatarPainter(url: String?) = rememberImagePainter(
        data = url,
        builder = {
            scale(Scale.FIT)
            transformations(circle)
            placeholder(R.mipmap.ic_user_default)
            error(R.mipmap.ic_user_default)
        }
)