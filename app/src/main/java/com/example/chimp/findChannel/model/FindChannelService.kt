package com.example.chimp.findChannel.model

import com.example.chimp.chats.model.channel.Channel
import com.example.chimp.chats.model.channel.ChannelName
import com.example.chimp.either.Either
import com.example.chimp.model.errors.ResponseErrors
import kotlinx.coroutines.flow.Flow

/**
 * Interface that defines the service used in FindChannelViewModel.
 */
interface FindChannelService {
    /**
     * Join a channel.
     * @param channelId the id of the channel to join
     * @return an [Either] with [Unit] if the channel was joined,
     * or a [ResponseErrors] if it failed.
     */
    suspend fun joinChannel(channelId: UInt): Either<ResponseErrors, Unit>

    /**
     * Find a channel by its name.
     *
     * @param channelName the name of the channel to find
     * @return an [Either] with the [Channel] if the channel was found,
     * or a [ResponseErrors] if it failed.
     */
    suspend fun findChannel(channelName: ChannelName): Either<ResponseErrors, FindChannelItem>

    /**
     * Get a list of channels.
     *
     * @param offset the offset to start fetching channels from
     * @param limit the maximum number of channels to fetch
     * @return an [Either] with a [Flow] of [Channel] if the channels were found,
     * or a [ResponseErrors] if it failed.
     */
    suspend fun getChannels(offset: UInt?, limit: UInt?): Either<ResponseErrors, Flow<FindChannelItem>>
}