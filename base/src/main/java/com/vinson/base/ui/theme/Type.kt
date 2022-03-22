package com.vinson.base.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Base = TextStyle(
        color = Text100
)

val Bold = TextStyle(
        fontWeight = FontWeight.Bold
)

val BigTitle = Base + TextStyle(
        fontSize = 26.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.62).sp
)

val BoldBigTitle = BigTitle.merge(Bold)

val Title = Base + TextStyle(
        fontSize = 18.sp,
        lineHeight = 25.sp,
        letterSpacing = (-0.43).sp
)

val BoldTitle = Title.merge(Bold)

val Body = Base + TextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.38).sp
)

val BoldBody = Body.merge(Bold)

val Caption = Base + TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.34).sp
)

val BoldCaption = Caption.merge(Bold)