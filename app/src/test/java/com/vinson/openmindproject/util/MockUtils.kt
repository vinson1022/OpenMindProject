@file:JvmName("MockUtils")
package com.vinson.openmindproject.util

import androidx.annotation.StringRes
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject

fun mockGetString(@StringRes resId: Int = -1, result: String = "") {
    every { appCxt().getString(resId.takeIf { it != -1 } ?: any()) } returns result
}

fun mockPrivateField(target: Any, filedName: String, mockValue: Any): Any? {
    return target.javaClass.declaredFields.find { it.name == filedName }?.also {
        it.isAccessible = true
        it.set(target, mockValue)
    }?.get(target)
}

fun getPrivateField(target: Any, filedName: String): Any? {
    return target.javaClass.declaredFields.find { it.name == filedName }?.also {
        it.isAccessible = true
    }?.get(target)
}