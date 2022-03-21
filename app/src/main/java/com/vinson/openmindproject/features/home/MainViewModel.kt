package com.vinson.openmindproject.features.home

import androidx.compose.runtime.*
import androidx.paging.PagingData
import com.vinson.base.ui.util.BaseLoadingViewModel
import com.vinson.datamodel.asset.Asset
import com.vinson.datamodel.asset.AssetDetail
import com.vinson.openmindproject.model.AssetRepository
import kotlinx.coroutines.flow.*

class MainViewModel(
    private val repository: AssetRepository
): BaseLoadingViewModel() {

    val state: HomeState by mutableStateOf(HomeState())

    init {
        getAssets()
        getAssetDetail("0xca063e5e22ddcb8b7a8f4ccb9d3da4107be9dce6", "12")
    }

    private fun getAssets() {
        state.setupAssets(repository.getAssets().flow)
    }

    private fun getAssetDetail(address: String, token: String) {
        sendApi {
            repository.getAssetDetail(address, token).handleResult {
                state.setupDetail(it)
            }
        }
    }

    class HomeState {
        var assets: Flow<PagingData<Asset>> by mutableStateOf(flowOf())
            private set
        var assetDetail: AssetDetail? by mutableStateOf(null)

        fun setupAssets(flow: Flow<PagingData<Asset>>) {
            assets = flow
        }

        fun setupDetail(detail: AssetDetail) {
            assetDetail = detail
        }
    }
}