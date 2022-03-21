package com.vinson.openmindproject.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vinson.api.api.OpenSeaApi
import com.vinson.datamodel.asset.Asset
import com.vinson.datamodel.base.Result
import com.vinson.openmindproject.util.getResult
import com.vinson.openmindproject.util.safeApiCall

class AssetDataSource {
    private val api = OpenSeaApi.create()

    fun getAssets() = object : PagingSource<Int, Asset>() {

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Asset> {
            return try {
                val nextPage = params.key ?: 0
                val response = api.getAssetDetail(offset = nextPage)

                when (val result = response.getResult()) {
                    is Result.Success -> {
                        val isOverFlow = response.body()?.getData()?.size ?: 0 < 20
                        LoadResult.Page(
                            data = result.data,
                            prevKey = null,
                            nextKey = if (isOverFlow) null else nextPage.plus(1)
                        )
                    }
                    is Result.Error -> {
                        LoadResult.Error(result.exception)
                    }
                }
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, Asset>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                val anchorPage = state.closestPageToPosition(anchorPosition)
                anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
            }
        }
    }

    suspend fun getAssetDetail(address: String, token: String) = safeApiCall {
        api.getAssetDetail(address, token).getResult()
    }
}