package com.vinson.datamodel.asset

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Collection(
    @SerializedName("name") val name: String,
) : Parcelable
