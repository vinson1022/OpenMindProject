package com.vinson.openmindproject.util

/**
 * This annotation is just only a tag for unit test.
 * We don't want a lot of kinds of string (es: "// GIVEN" ) in unit test code
 */
@Target(AnnotationTarget.LOCAL_VARIABLE, AnnotationTarget.EXPRESSION)
@Retention(AnnotationRetention.SOURCE)
annotation class Tag(val value: Int)

const val GIVEN = 0
const val WHEN = 1
const val THEN = 2