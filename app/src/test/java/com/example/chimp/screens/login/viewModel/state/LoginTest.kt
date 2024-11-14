package com.example.chimp.screens.login.viewModel.state

import org.junit.Test

class LoginTest {
    @Test
    fun isValidTest() {
        val login = Login.LoginShow("username", "P@ssw0rd")
        assert(login.isValid)
    }

    @Test
    fun isNotValidTest() {
        val login = Login.LoginHide("", "password")
        assert(!login.isValid)
    }
}