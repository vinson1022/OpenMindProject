package com.vinson.datamodel.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.vinson.datamodel.asset.Asset
import com.vinson.datamodel.base.BaseResponse
import com.vinson.datamodel.base.ServerErrorException
import kotlinx.parcelize.Parcelize

@Parcelize
data class AssetsResponse(
    @SerializedName("assets") private val assets: List<Asset>
) : Parcelable, BaseResponse<List<Asset>> {

    override fun isSuccess() = assets.size == 20

    override fun getData() = assets

    override fun getError() = ServerErrorException(403, "error")
}
