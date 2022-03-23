package com.vinson.openmindproject.model

class EthRepository private constructor() {

    private val dataSource = EthDataSource()

    suspend fun getBalance() = dataSource.getBalance()

    companion object {
        private var INSTANCE: EthRepository? = null

        @JvmStatic
        fun getInstance() = INSTANCE ?: synchronized(EthRepository::class.java) {
            INSTANCE ?: EthRepository().also { INSTANCE = it }
        }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}