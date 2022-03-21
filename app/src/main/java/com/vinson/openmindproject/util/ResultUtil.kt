package com.vinson.openmindproject.util

import com.vinson.base.architecture.Cache
import com.vinson.datamodel.base.BaseResponse
import com.vinson.datamodel.base.NetworkErrorException
import com.vinson.datamodel.base.Result
import com.vinson.datamodel.base.ServerErrorException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import retrofit2.Response

fun <T : Any, R : BaseResponse<T>> Response<R>.getResult() = getResult { it }

fun <T : Any, S : BaseResponse<T>, R> Response<S>.getResult(mapping: (T) -> R): Result<R> {
    return if (isSuccessful) {
        val data = body()?.getData()
        if (data != null) {
            Result.Success(mapping(data))
        } else {
            Result.Error(ServerErrorException(code(), "Unexpected response body: null"))
        }
    } else {
        getErrorResult()
    }
}

fun <T : Any, R : BaseResponse<T>> Response<R>.getNoDataResult(): Result<T?> {
    return if (isSuccessful) {
        Result.Success(null)
    } else {
        getErrorResult()
    }
}

private fun <T : Any, S : BaseResponse<T>, R> Response<S>.getErrorResult(): Result<R> {
    return Result.Error(ServerErrorException(code(), errorBody()?.string()))
}

fun <T : Any> Result<T>.saveToCache(cache: Cache<T>) {
    if (this is Result.Success) cache.set(data)
}

fun <T> Cache<T>.getSuccessResult() = get()?.let { Result.Success(it) }

/**
 * CAREFUL!
 * This function force cast [Result] to [Result.Error].
 * Only call this function if you 100% sure the receiver is an instance of [Result.Error]
 * */
fun <T> Result<T>.mapError() = Result.Error((this as Result.Error).exception)

fun <T, R> Result<T>.map(mapping: (T) -> R) = when (this) {
    is Result.Success -> Result.Success(mapping(data))
    is Result.Error -> Result.Error(exception)
}


/**
 * Wrap a suspending API [call] in try/catch. In case an exception is thrown,
 * a no internet [Result.Error] is created automatically.
 */
suspend fun <T : Any?> safeApiCall(call: suspend () -> Result<T>): Result<T> {
    return try {
        withContext(IO) {
            call()
        }
    } catch (e: Throwable) {
        e.printStackTrace()
        // An exception was thrown when calling the API
        Result.Error(NetworkErrorException)
    }
}