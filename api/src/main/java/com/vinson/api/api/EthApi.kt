package com.vinson.api.api

import com.vinson.api.builder.ApiType
import com.vinson.api.builder.getOkHttpClient
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import java.math.BigInteger

object EthApi {

    private val web3j by lazy {
        Web3j.build(
                HttpService(
                    "https://ropsten.infura.io/v3/f540367abbab400c8e0fac559099d10a",
                    getOkHttpClient(ApiType.Eth),
                    false
                )
            )
    }

    fun getBalance(address: String): BigInteger? = web3j
        .ethGetBalance(address, DefaultBlockParameterName.LATEST)
        .send()
        .balance
}