package com.vinson.openmindproject

import io.mockk.*
import org.junit.After
import org.junit.Before

open class BaseTest {
    @Before
    open fun setUp() {
        mockkObject(App.Companion)
        every { App.context } returns mockk()
        every { App.getInstance() } returns mockk()
    }

    @After
    open fun end() {
        unmockkAll()
    }
}