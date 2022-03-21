package com.vinson.datamodel.asset

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contract(
    @SerializedName("address") val address: String,
) : Parcelable
