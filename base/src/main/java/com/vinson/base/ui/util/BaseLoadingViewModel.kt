package com.vinson.base.ui.util

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseLoadingViewModel : BaseViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading
    protected open val scope = viewModelScope

    open fun sendApi(apiAction: suspend CoroutineScope.() -> Unit) {
        scope.launch {
            _isLoading.emit(true)
            apiAction()
            _isLoading.emit(false)
        }
    }
}