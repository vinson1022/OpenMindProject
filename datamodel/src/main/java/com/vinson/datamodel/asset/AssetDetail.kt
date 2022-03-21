package com.vinson.datamodel.asset

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.vinson.datamodel.base.BaseResponse
import com.vinson.datamodel.base.ServerErrorException
import kotlinx.parcelize.Parcelize

@Parcelize
data class AssetDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("image_url") val image: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("collection") val collection: Collection,
    @SerializedName("permalink") val link: String,
) : Parcelable, BaseResponse<AssetDetail> {

    override fun isSuccess() = true

    override fun getData() = this

    override fun getError() = ServerErrorException(403, "error")

}
