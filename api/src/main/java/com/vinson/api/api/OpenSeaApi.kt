package com.vinson.api.api

import com.vinson.api.builder.ApiType
import com.vinson.api.builder.getOkHttpClient
import com.vinson.datamodel.api.AssetsResponse
import com.vinson.datamodel.asset.AssetDetail
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenSeaApi {

    @GET("assets")
    suspend fun getAssetDetail(
        @Query("offset") offset: Int,
        @Query("format") format: String = "json",
        @Query("owner") owner: String = "0x19818f44faf5a217f619aff0fd487cb2a55cca65",
        @Query("limit") limit: Int = 20
    ): Response<AssetsResponse>

    @GET("asset/{contract_address}/{token_id}")
    suspend fun getAssetDetail(
        @Path("contract_address") address: String,
        @Path("token_id") token: String
    ): Response<AssetDetail>

    companion object {

        private fun getBaseUrl() = "https://api.opensea.io/api/v1/"

        fun create(): OpenSeaApi {
            return Retrofit.Builder()
                    .baseUrl(getBaseUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClient(ApiType.OpenSea))
                    .build()
                    .create(OpenSeaApi::class.java)
        }
    }
}