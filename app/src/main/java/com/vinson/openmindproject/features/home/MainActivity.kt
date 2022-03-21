package com.vinson.openmindproject.features.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vinson.base.ui.theme.BoldTitle
import com.vinson.base.ui.theme.Text10
import com.vinson.openmindproject.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setContent {
            MainLayout()
        }
    }
}

@Composable
fun MainLayout() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = Text10, darkIcons = true)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Text10)
    ) {
        Text(
            "Hello World",
            modifier = Modifier.fillMaxSize(),
            style = BoldTitle,
            textAlign = TextAlign.Center
        )
    }
}