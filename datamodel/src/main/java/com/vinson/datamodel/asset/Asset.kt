package com.vinson.datamodel.asset

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Asset(
    @SerializedName("id") val id: Int,
    @SerializedName("image_url") val image: String,
    @SerializedName("name") val name: String,
    @SerializedName("asset_contract") val contract: Contract,
    @SerializedName("token_id") val token: String,
) : Parcelable
