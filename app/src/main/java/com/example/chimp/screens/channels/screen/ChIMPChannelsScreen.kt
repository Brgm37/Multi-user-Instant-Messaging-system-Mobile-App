package com.example.chimp.screens.channels.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.chimp.screens.channels.screen.view.JoiningView
import com.example.chimp.screens.ui.views.ChannelInfoView
import com.example.chimp.screens.channels.screen.view.ScrollingView
import com.example.chimp.screens.channels.viewModel.ChannelsViewModel
import com.example.chimp.screens.channels.viewModel.state.ChannelsScreenState
import com.example.chimp.screens.ui.composable.MenuBottomBar
import com.example.chimp.screens.ui.views.ErrorView
import com.example.chimp.screens.ui.views.LoadingView
import com.example.chimp.screens.ui.views.ShowingInvitationView

const val CHANNELS_SCREEN_TAG = "ChannelsScreen"

@Composable
internal fun ChIMPChannelsScreen(
    modifier: Modifier = Modifier,
    vm: ChannelsViewModel,
    onFindChannelNavigate: () -> Unit = {},
    onAboutNavigate: () -> Unit = {},
    onRegisterNavigate: () -> Unit = {},
    onChannelNavigate: () -> Unit = {},
    onCreateChannelNavigate: () -> Unit = {},
) {

    val registerNavigate = {
        onRegisterNavigate()
        vm.reset()
    }

    Column(
        modifier = modifier
    ) {
        val curr = vm.state.collectAsState().value
        Log.i(CHANNELS_SCREEN_TAG, "State: ${curr::class.simpleName}")
        when (curr) {
            is ChannelsScreenState.Initial -> {
                vm.loadChannels()
            }

            is ChannelsScreenState.Loading -> {
                ChatsScreenAux(
                    bottomBar = {
                        MenuBottomBar(
                            chatsIsEnable = false,
                            findChannelClick = onFindChannelNavigate,
                            aboutClick = onAboutNavigate,
                            createChannelClick = onCreateChannelNavigate
                        )
                    }
                ) { modifier ->
                    LoadingView(
                        modifier = modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center),
                    )
                }
            }

            is ChannelsScreenState.Scrolling -> {
                ChatsScreenAux(
                    bottomBar = {
                        MenuBottomBar(
                            chatsIsEnable = false,
                            findChannelClick = onFindChannelNavigate,
                            aboutClick = onAboutNavigate,
                            createChannelClick = onCreateChannelNavigate
                        )
                    }
                ) { modifier ->
                    ScrollingView(
                        modifier = modifier,
                        chats = curr,
                        onLogout = vm::logout,
                        onReload = vm::reset,
                        onDeleteOrLeave = vm::deleteOrLeave,
                        onInfoClick = vm::toChannelInfo,
                        onChannelClick = { vm.onChannelClick(it, onChannelNavigate) },
                        onLoadMore = vm::loadMore,
                        onCreateUserInvitation = vm::toUserInvitation,
                        onJoinClick = vm::toJoinChannel,
                    )
                }
            }

            is ChannelsScreenState.Error -> {
                ErrorView(
                    modifier = modifier.fillMaxSize(),
                    tryAgain = vm::goBack,
                    error = curr.error
                )
            }

            is ChannelsScreenState.Info -> {
                ChannelInfoView(
                    modifier = Modifier.fillMaxSize(),
                    channel = curr.channel,
                    onGoBackClick = vm::goBack
                )
            }
            is ChannelsScreenState.BackToRegistration -> {
                vm.backToRegistration()
                registerNavigate()
            }
            is ChannelsScreenState.CreateUserInvitation -> {
                ShowingInvitationView(
                    modifier = Modifier.fillMaxSize(),
                    inviteCode = curr.inviteCode,
                    onDismiss = vm::goBack
                )
            }

            is ChannelsScreenState.JoiningChannel -> {
                JoiningView(
                    modifier = Modifier
                        .fillMaxSize(),
                    onJoinChannel = { invitationCode ->
                        vm.joinChannel(
                            invitationCode,
                            onChannelNavigate
                        )
                    },
                    onBackClick = vm::goBack
                )
            }
        }
    }
}

@Composable
private fun ChatsScreenAux(
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        bottomBar = bottomBar
    ) { innerPadding ->
        content(Modifier.padding(innerPadding))
    }
}