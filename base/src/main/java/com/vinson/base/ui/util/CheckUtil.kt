package com.vinson.base.ui.util

import java.util.regex.Pattern

private const val EMAIL_REGEX = "\\A(|(([A-Za-z0-9]+_+)|([A-Za-z0-9]+\\-+)|([A-Za-z0-9]+\\.+)|([A-Za-z0-9]+\\++))*[A-Za-z0-9]+@((\\w+\\-+)|(\\w+\\.))*\\w{1,63}\\.[a-zA-Z]{2,6})+\\z"
private const val PASSWORD_MIN_LENGTH = 8
private const val PASSWORD_MAX_LENGTH = 36

fun isEmailValid(email: String?)
        = !email.isNullOrEmpty() && Pattern.compile(EMAIL_REGEX).matcher(email).matches()

fun isPasswordValid(password: String?)
        = password != null && (password.length in PASSWORD_MIN_LENGTH..PASSWORD_MAX_LENGTH)

fun isFloatValid(value: String?)
        = value?.toFloatOrNull() != null