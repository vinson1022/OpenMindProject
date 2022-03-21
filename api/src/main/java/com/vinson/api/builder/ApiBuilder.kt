package com.vinson.api.builder

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

fun getOkHttpClient(type: ApiType): OkHttpClient {
    return OkHttpClient().newBuilder().apply {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
        addInterceptor(interceptor)
        addInterceptor(getHeaderInterceptor(type))
    }.build()
}

private fun getHeaderInterceptor(type: ApiType): Interceptor {
    return Interceptor { chain ->
        var request = chain.request()

        val url = request.url()
        val body = request.body()

        request = request.newBuilder().apply {
            url(url)
            when (type) {
                ApiType.OpenSea -> generateHeader()
            }
            method(request.method(), body)

        }.build()


        Log.d("OkHttp", request.headers().toString())
        chain.proceed(request)
    }
}

private fun Request.Builder.generateHeader() {
    header("X-API-KEY", "5b294e9193d240e39eefc5e6e551ce83")
}

enum class ApiType {
    OpenSea
}