package com.example.chimp.screens.login.viewModel.state

import com.example.chimp.models.errors.ResponseErrors
import com.example.chimp.models.users.User

/**
 * The state of the Login screen.
 */
internal sealed interface LoginScreenState {
    /**
     * The screen is loading.
     */
    data object Loading : LoginScreenState

    /**
     * The screen has an error.
     *
     * @property error the error that occurred
     */
    data class Error(val error: ResponseErrors) : LoginScreenState

    /**
     * The screen is successful.
     *
     * @property user the user that logged in
     */
    data class Success(val user: User) : LoginScreenState
}