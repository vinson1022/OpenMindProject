package com.vinson.openmindproject.features.home

import androidx.compose.runtime.*
import androidx.paging.PagingData
import com.vinson.base.ui.util.BaseLoadingViewModel
import com.vinson.datamodel.asset.Asset
import com.vinson.datamodel.asset.AssetDetail
import com.vinson.openmindproject.model.AssetRepository
import com.vinson.openmindproject.model.EthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: AssetRepository,
    private val ethRepository: EthRepository
): BaseLoadingViewModel() {

    val state: HomeState by mutableStateOf(HomeState())

    init {
        getAssets()
        getBalance()
    }

    private fun getAssets() {
        state.setupAssets(repository.getAssets().flow)
    }

    private fun getBalance() {
        sendApi {
            ethRepository.getBalance().handleResult {
                state.setupBalance(it.toString())
            }
        }
    }

    private fun getAssetDetail(address: String, token: String) {
        sendApi {
            repository.getAssetDetail(address, token).handleResult {
                state.setupDetail(it)
            }
        }
    }

    override fun sendApi(apiAction: suspend CoroutineScope.() -> Unit) {
        scope.launch {
            state.pageState = HomePageState.Loading
            apiAction()
        }
    }

    fun handleEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.OpenDetail -> {
                with(event.asset) {
                    getAssetDetail(contract.address, token)
                }
            }
            HomeEvent.ExitDetail -> {
                state.exitDetail()
            }
        }
    }

    class HomeState {
        var assets: Flow<PagingData<Asset>> by mutableStateOf(flowOf())
            private set
        var assetDetail: AssetDetail? by mutableStateOf(null)
        var pageState: HomePageState by mutableStateOf(HomePageState.Main)
        var balance: String by mutableStateOf("")

        fun setupAssets(flow: Flow<PagingData<Asset>>) {
            assets = flow
        }

        fun setupDetail(detail: AssetDetail) {
            pageState = HomePageState.Detail
            assetDetail = detail
        }

        fun setupBalance(value: String) {
            pageState = HomePageState.Main
            balance = value
        }

        fun exitDetail() {
            pageState = HomePageState.Main
            assetDetail = null
        }
    }

    sealed class HomeEvent {
        class OpenDetail(val asset: Asset): HomeEvent()
        object ExitDetail: HomeEvent()
    }

    enum class HomePageState(val route: String) {
        Loading("loading"), Main("main"), Detail("detail")
    }
}