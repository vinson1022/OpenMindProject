package com.vinson.datamodel.base

interface BaseResponse<T> {
    fun isSuccess():Boolean
    fun getData(): T
    fun getError(): Exception
}
