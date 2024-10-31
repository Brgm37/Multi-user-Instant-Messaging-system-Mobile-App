package com.example.chimp.login.screen.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chimp.R
import com.example.chimp.login.screen.composable.BaseView
import com.example.chimp.login.screen.composable.MakePasswordTextField
import com.example.chimp.login.screen.composable.MakeUsernameTextField
import com.example.chimp.login.viewModel.state.Register
import com.example.chimp.login.viewModel.state.Visibility
import com.example.chimp.ui.composable.MakeButton
import com.example.chimp.ui.composable.MySpacer

/**
 * The tag used to identify the register view in automated tests.
 */
const val REGISTER_VIEW_TEST_TAG = "RegisterViewTestTag"

/**
 * The horizontal padding of the text field.
 */
private const val HORIZONTAL_PADDING = 10

/**
 * The RegisterView composable that displays the register screen.
 *
 * @param modifier the modifier to be applied to the composable
 * @param vm the view model that holds the state of the register screen
 * @param onUsernameChange the callback to be invoked when the username changes
 * @param onPasswordChange the callback to be invoked when the password changes
 * @param onConfirmPasswordChange the callback to be invoked when the confirm password changes
 * @param isToShowChange the callback to be invoked when the user wants to show/hide the password
 */
@Composable
fun RegisterView(
    modifier: Modifier = Modifier,
    vm: Register,
    onUsernameChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onConfirmPasswordChange: (String) -> Unit = {},
    onRegisterChange: () -> Unit = {},
    onLoginChange: () -> Unit = {},
    isToShowChange: () -> Unit = {},
) {
    BaseView(
        modifier = modifier.testTag(REGISTER_VIEW_TEST_TAG),
        visibility = vm as Visibility,
    ) { isToShow ->
        MySpacer()
        Text(
            text = stringResource(R.string.register_message),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black,
        )
        MySpacer()
        MakeUsernameTextField(
            value = vm.username,
            onUsernameChange = onUsernameChange
        )
        MySpacer()
        MakePasswordTextField(
            value = vm.password,
            label = stringResource(R.string.password),
            onPasswordChange = onPasswordChange,
            isToShow = isToShow,
            isToShowChange = isToShowChange
        )
        MySpacer()
        MakePasswordTextField(
            value = vm.confirmPassword,
            label = stringResource(R.string.confirm_password),
            onPasswordChange = onConfirmPasswordChange,
            isToShow = isToShow,
            isToShowChange = isToShowChange
        )
        MySpacer()
        MakeButton(
            modifier = Modifier.padding(HORIZONTAL_PADDING.dp),
            text = stringResource(R.string.register),
            enable = vm.isValid,
            onClick = onRegisterChange
        )
        MySpacer()
        MakeButton(
            modifier = Modifier.padding(HORIZONTAL_PADDING.dp),
            text = stringResource(R.string.login),
            onClick = onLoginChange
        )
    }
}

@Preview
@Composable
private fun PreviewRegisterView() {
    var vm: Register by remember { mutableStateOf(Register.RegisterHide("", "")) }
    RegisterView(
        vm = vm,
        onUsernameChange = { vm = vm.updateUsername(it) },
        onPasswordChange = { vm = vm.updatePassword(it) },
        onRegisterChange = { },
        onLoginChange = { },
        isToShowChange = { },
    )
}