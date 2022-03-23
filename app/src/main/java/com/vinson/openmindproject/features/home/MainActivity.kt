package com.vinson.openmindproject.features.home

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vinson.base.ui.component.LoadingContent
import com.vinson.base.ui.theme.Text10
import com.vinson.openmindproject.features.home.MainViewModel.HomePageState.*
import com.vinson.openmindproject.features.home.page.DetailPage
import com.vinson.openmindproject.features.home.page.MainPage
import com.vinson.openmindproject.model.AssetRepository
import com.vinson.openmindproject.model.EthRepository
import com.vinson.openmindproject.util.getViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainLayout()
        }
    }
}

@Composable
fun MainLayout() {
    val activity = LocalContext.current as AppCompatActivity

    val viewModel = activity.getViewModel {
        MainViewModel(AssetRepository.getInstance(), EthRepository.getInstance())
    }
    val navController = rememberNavController()

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = Text10, darkIcons = true)

    LaunchedEffect(key1 = viewModel.state.pageState) {
        navController.navigate(viewModel.state.pageState.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Text10)
    ) {
        NavHost(
            navController = navController,
            startDestination = Main.route,
        ) {
            composable(Loading.route) { LoadingContent() }
            composable(Main.route) {
                MainPage(
                    viewModel.state,
                ) {
                    viewModel.handleEvent(it)
                }
            }
            composable(Detail.route) {
                DetailPage(viewModel.state) {
                    viewModel.handleEvent(it)
                }
            }
        }
    }
}