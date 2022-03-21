package com.vinson.base.ui.util

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import com.vinson.datamodel.base.Result

abstract class BaseViewModel : ViewModel() {
    private val _errorException = MutableStateFlow<Exception?>(null)
    val errorException: StateFlow<Exception?>
        get() = _errorException
    val errorMsgEvent = _errorException.map { it?.message }

    protected suspend fun <T> Result<T>.handleResult(successAction: suspend (T) -> Unit = {}) {
        _errorException.value = null
        when (this) {
            is Result.Success -> {
                successAction(data)
            }
            is Result.Error -> {
                handleError()
            }
        }
    }

    protected suspend fun List<Result<Any>>.handleResults(successAction: suspend () -> Unit) {
        when {
            all { it is Result.Success } -> successAction()
            else -> (find { it is Result.Error } as? Result.Error)?.handleError()
        }
    }

    protected open suspend fun Result.Error.handleError() {
        _errorException.emit(exception)
    }
}