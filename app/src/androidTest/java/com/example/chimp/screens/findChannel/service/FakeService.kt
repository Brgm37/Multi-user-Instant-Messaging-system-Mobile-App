package com.example.chimp.screens.findChannel.service

import com.example.chimp.models.channel.ChannelInfo
import com.example.chimp.models.channel.ChannelName
import com.example.chimp.models.either.Either
import com.example.chimp.models.either.failure
import com.example.chimp.models.either.success
import com.example.chimp.models.errors.ResponseError
import com.example.chimp.models.users.UserInfo
import com.example.chimp.observeConnectivity.ConnectivityObserver.*
import com.example.chimp.screens.findChannel.model.FindChannelService
import com.example.chimp.screens.findChannel.model.FindChannelsResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeService: FindChannelService {
    private val controller = Channel<Unit>()
    private val flow = MutableStateFlow(emptyList<ChannelInfo>())
    suspend fun unlock() = controller.send(Unit)

    private val channelName = ChannelName("test", "test")
    private val user = UserInfo(1u, "test")
    private val cId = 1u
    override val connectivity: Flow<Status>
        get() = flowOf(Status.CONNECTED)


    override suspend fun joinChannel(channelId: UInt): Either<ResponseError, ChannelInfo> {
        val channelNotFound = 2u
        controller.receive()
        return when (channelId) {
            cId -> success(ChannelInfo(cId, channelName, owner = user))
            channelNotFound -> failure(ResponseError("Channel not found", "https://example.com"))
            else -> failure(ResponseError.Unauthorized)
        }
    }

    override suspend fun getChannels(name: String): Either<ResponseError, FindChannelsResult> {
        controller.receive()
        return success(FindChannelsResult(flow, flowOf(true)))
    }

    override suspend fun getChannels(): Either<ResponseError, FindChannelsResult> {
        controller.receive()
        return success(FindChannelsResult(flow, flowOf(true)))
    }

    override suspend fun fetchMore(): Either<ResponseError, Unit> {
        controller.receive()
        flow.emit(flow.value + ChannelInfo(
            cId,
            channelName,
            owner = user
        )
        )
        return success(Unit)
    }

    override suspend fun fetchMore(name: String): Either<ResponseError, Unit> {
        controller.receive()
        flow.emit(flow.value + ChannelInfo(
            cId,
            channelName,
            owner = user
        )
        )
        return success(Unit)
    }
}