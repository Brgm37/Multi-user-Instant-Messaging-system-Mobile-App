package com.example.chimp.model.utils

import org.junit.Test

class EitherTest {
    @Test
    fun testSuccess() {
        val either = success("Hello")
        assert(either.isSuccess())
        assert(either.value == "Hello")
    }

    @Test
    fun testFailure() {
        val either = failure("Error")
        assert(either.isFailure())
        assert(either.value == "Error")
    }
}