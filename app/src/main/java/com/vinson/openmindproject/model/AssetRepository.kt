package com.vinson.openmindproject.model

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vinson.base.architecture.Cache
import com.vinson.datamodel.asset.AssetDetail
import com.vinson.openmindproject.util.getSuccessResult
import com.vinson.openmindproject.util.saveToCache

class AssetRepository private constructor() {

    private val dataSource = AssetDataSource()
    private val cachedDetails = hashMapOf<String, Cache<AssetDetail>>()

    fun getAssets() = Pager(config = PagingConfig(20)) {
        dataSource.getAssets()
    }

    suspend fun getAssetDetail(address: String, token: String) =
        cachedDetails["$address/$token"]?.getSuccessResult()
            ?: dataSource.getAssetDetail(address, token).also {
                val cache = cachedDetails["$address/$token"] ?: Cache<AssetDetail>().also { cache ->
                    cachedDetails["$address/$token"] = cache
                }
                it.saveToCache(cache)
            }

    companion object {
        private var INSTANCE: AssetRepository? = null

        @JvmStatic
        fun getInstance() = INSTANCE ?: synchronized(AssetRepository::class.java) {
            INSTANCE ?: AssetRepository().also { INSTANCE = it }
        }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}