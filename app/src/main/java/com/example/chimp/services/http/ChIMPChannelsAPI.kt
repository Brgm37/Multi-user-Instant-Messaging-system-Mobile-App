package com.example.chimp.services.http

import android.util.Log
import androidx.compose.runtime.collectAsState
import com.example.chimp.models.either.Either
import com.example.chimp.models.errors.ResponseError
import com.example.chimp.models.either.failure
import com.example.chimp.models.either.success
import com.example.chimp.models.users.User
import com.example.chimp.models.channel.ChannelInfo
import com.example.chimp.observeConnectivity.ConnectivityObserver.Status
import com.example.chimp.observeConnectivity.ConnectivityObserver.Status.DISCONNECTED
import com.example.chimp.screens.channels.model.ChannelsServices
import com.example.chimp.screens.channels.model.FetchChannelsResult
import com.example.chimp.services.http.dtos.input.channel.ChannelInputModel
import com.example.chimp.services.http.dtos.input.error.ErrorInputModel
import com.example.chimp.services.http.dtos.input.channel.ChannelListInputModel
import com.example.chimp.services.http.dtos.input.channel.toChannelInfo
import com.example.chimp.services.http.utlis.makeHeader
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.sse.sse
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.minutes

/**
 * ChIMPChannelsAPI is the implementation of the ChannelsServices interface.
 *
 * This class is responsible for handling the HTTP requests related to the channels.
 *
 * @property client the HTTP client used to make the requests
 * @property url the base URL for the requests
 * @property user the flow of the current user
 */
class ChIMPChannelsAPI(
    private val client: HttpClient,
    private val url: String,
    private val user: Flow<User?>,
    private val connection: Flow<Status>
) : ChannelsServices {
    private val _channels = MutableStateFlow<List<ChannelInfo>>(emptyList())
    private val _hasMore = MutableStateFlow(false)
    private var idx = 0
    private val limit = 10
    private val hasMore = limit + 1
    private val api = "$url/api/channels"
    private val _conn = MutableStateFlow(DISCONNECTED)

    override suspend fun fetchChannels(): Either<ResponseError, FetchChannelsResult> {
        val curr = user.first() ?: return failure(ResponseError.Unauthorized)
        _conn.first().let { conn ->
            if (conn == DISCONNECTED) return failure(ResponseError.NoInternet)
        }
        idx = 0
        client
            .get("$api/my?limit=$hasMore") { makeHeader(curr) }
            .let { response ->
                try {
                    return when(response.status) {
                        HttpStatusCode.OK -> {
                            val channels: List<ChannelListInputModel> = Json.decodeFromString(response.bodyAsText())
                            val hasMore = channels.size == hasMore
                            _channels.emit(channels.toChannelInfo().take(limit))
                            _hasMore.emit(hasMore)
                            success(FetchChannelsResult(_channels, _hasMore))
                        }
                        HttpStatusCode.Unauthorized -> failure(ResponseError.Unauthorized)
                        else -> failure(response.body<ErrorInputModel>().toResponseError())
                    }
                } catch (e: Exception) {
                    Log.e(CHANNELS_SERVICE_TAG, "Error: ${e.message}")
                    return failure(e.message?.let { ResponseError(cause = it) }
                        ?: ResponseError.Unknown)
                }
            }
    }

    override suspend fun deleteOrLeave(channel: ChannelInfo): Either<ResponseError, Unit> {
        val curr = user.first() ?: return failure(ResponseError.Unauthorized)
        _conn.first().let { conn ->
            if (conn == DISCONNECTED) return failure(ResponseError.NoInternet)
        }
//        val currConn = connection.first()
//        if (currConn == DISCONNECTED) return failure(ResponseError.NoInternet)
        client
            .delete("$api/${channel.cId}") { makeHeader(curr) }
            .let { response ->
                try {
                    if (response.status == HttpStatusCode.OK) {
                        _channels.emit(_channels.value.filter { it.cId != channel.cId })
                        return success(Unit)
                    } else {
                        return failure(response.body<ErrorInputModel>().toResponseError())
                    }
                } catch (e: Exception) {
                    Log.e(CHANNELS_SERVICE_TAG, "Error: ${e.message}")
                    return failure(e.message?.let { ResponseError(cause = it) }
                        ?: ResponseError.Unknown)
                }
            }
    }

    override suspend fun fetchMore(): Either<ResponseError, Unit> {
        val curr = user.first() ?: return failure(ResponseError.Unauthorized)
        _conn.first().let { conn ->
            if (conn == DISCONNECTED) return failure(ResponseError.NoInternet)
        }
//        val currConn = connection.first()
//        if (currConn == DISCONNECTED) return failure(ResponseError.NoInternet)
        Log.i(CHANNELS_SERVICE_TAG, "Fetching more channels")
        idx += limit
        client
            .get("$api/my?offset=$idx&limit=$hasMore") { makeHeader(curr) }
            .let { response ->
                try {
                    return if (response.status == HttpStatusCode.OK) {
                        val channels = response.body<List<ChannelListInputModel>>().toChannelInfo()
                        val hasMore = channels.size == hasMore
                        _channels.emit(_channels.value + channels.take(limit))
                        _hasMore.emit(hasMore)
                        success(Unit)
                    } else {
                        failure(response.body<ErrorInputModel>().toResponseError())
                    }
                } catch (e: Exception) {
                    Log.e(CHANNELS_SERVICE_TAG, "Error: ${e.message}")
                    return failure(e.message?.let { ResponseError(cause = it) }
                        ?: ResponseError.Unknown)
                }
            }
    }

    override suspend fun initSseOnChannels(): Either<ResponseError, Unit> {
        val curr = user.first() ?: return failure(ResponseError.Unauthorized)
        _conn.first().let { conn ->
            if (conn == DISCONNECTED) return failure(ResponseError.NoInternet)
        }
//        val currConn = connection.first()
//        if (currConn == DISCONNECTED) return failure(ResponseError.NoInternet)
        try {
            client.sse(
                urlString = "$api/sse",
                request = { makeHeader(curr) },
                reconnectionTime = RECONNECT_TIME.minutes
            ) {
                try {
                    while (true) {
                        val c = _conn.first()
                        if (c == DISCONNECTED) continue
                        incoming.collect { event ->
                            Log.i(CHANNELS_SERVICE_TAG, "Event: ${event.data}")
                            when (event.event) {
                                JOIN_OR_UPDATE -> {
                                    event.data?.let { data ->
                                        val channel = Json.decodeFromString<ChannelInputModel>(data).toChannelInfo()
                                        if (_channels.value.find { it.cId == channel.cId } == null) {
                                            _channels.emit(_channels.value + channel)
                                        } else {
                                            _channels.emit(_channels.value.map { c ->
                                                if (c.cId == channel.cId) channel else c
                                            })
                                        }
                                    }
                                }
                                DELETE_OR_LEAVE -> {
                                    event.data?.let { data ->
                                        val channel = Json.decodeFromString<UInt>(data)
                                        if (_channels.value.find { it.cId == channel } != null)
                                            _channels.emit(_channels.value.filter { it.cId != channel })
                                    }
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e(CHANNELS_SERVICE_TAG, "Error: ${e.message}")
                }
            }
        } catch (e: Exception) {
            Log.e(CHANNELS_SERVICE_TAG, "Error: ${e.message}")
            return failure(e.message?.let { ResponseError(cause = it) }
                ?: ResponseError.Unknown)
        }
        return success(Unit)
    }

    override suspend fun initConnectionObserver() {
        connection.collect { _conn.emit(it) }
    }

    companion object {
        const val CHANNELS_SERVICE_TAG = "ChannelsService"
        const val RECONNECT_TIME = 5
        const val JOIN_OR_UPDATE = "joinOrUpdate"
        const val DELETE_OR_LEAVE = "deleteOrLeave"
    }
}