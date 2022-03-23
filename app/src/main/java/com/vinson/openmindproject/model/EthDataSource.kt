package com.vinson.openmindproject.model

import com.vinson.api.api.EthApi
import com.vinson.datamodel.base.Result
import com.vinson.openmindproject.util.safeApiCall

class EthDataSource {

    suspend fun getBalance(address: String = "0x19818f44Faf5A217F619AFF0FD487CB2a55cCa65") = safeApiCall {
        Result.Success(EthApi.getBalance(address))
    }
}