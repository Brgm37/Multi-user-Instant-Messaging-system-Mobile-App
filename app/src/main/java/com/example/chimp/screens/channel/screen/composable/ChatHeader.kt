package com.example.chimp.screens.channel.screen.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.twotone.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chimp.R
import com.example.chimp.models.channel.ChannelInfo
import com.example.chimp.models.channel.ChannelName
import com.example.chimp.models.users.UserInfo

/**
 * The padding for the header.
 */
private const val HEADER_PADDING = 16

/**
 * The clip radius for the header.
 */
private const val HEADER_CLIP_RADIUS = 8

private const val SIZE = 40

const val CHAT_HEADER_TEST_TAG = "ChatHeader"

const val BUTTON_CHAT_HEADER_BACK_TEST_TAG = "ButtonChatHeaderBack"

const val BUTTON_CHAT_HEADER_INFO_TEST_TAG = "ButtonChatHeaderInfo"


/**
 * ChatHeader is a composable that displays the header of the scrolling view.
 *
 * @param onBackClick the callback to be called when the back button is clicked.
 * @param channel the channel to display.
 * @param onInfoClick the callback to be called when the info button is clicked.
 */
@Composable
internal fun ChatHeader(
    onBackClick: () -> Unit,
    channel: ChannelInfo,
    onInfoClick: (ChannelInfo) -> Unit
) {
    Row(
        modifier = Modifier
            .testTag(CHAT_HEADER_TEST_TAG)
            .fillMaxWidth()
            .clip(
                shape = RoundedCornerShape(
                    bottomEnd = HEADER_CLIP_RADIUS.dp,
                    bottomStart = HEADER_CLIP_RADIUS.dp
                )
            )
            .background(MaterialTheme.colorScheme.onSurface)
            .padding(HEADER_PADDING.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.testTag(BUTTON_CHAT_HEADER_BACK_TEST_TAG)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                tint = MaterialTheme.colorScheme.surface,
                contentDescription = "Back"
            )
        }
        Image(
            modifier = Modifier
                .size(SIZE.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = channel.icon),
            contentDescription = "channel Picture"
        )
        Text(
            text = channel.name.displayName,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.surface
        )
        IconButton(
            onClick = { onInfoClick(channel) },
            modifier = Modifier.testTag(BUTTON_CHAT_HEADER_INFO_TEST_TAG)
        ) {
            Icon(
                imageVector = Icons.TwoTone.MoreVert,
                tint = MaterialTheme.colorScheme.surface,
                contentDescription = "Options"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatHeaderPreview() {
    var isInfoDialogVisible by remember { mutableStateOf(false) }
    ChatHeader(
        onBackClick = {},
        channel = ChannelInfo(
            cId = 1u,
            name = ChannelName("Channel 1", "Channel 1"),
            owner = UserInfo(1u, "Owner 1"),
            icon = R.drawable.icon3
        ),
        onInfoClick = { isInfoDialogVisible = true }
    )
    if (isInfoDialogVisible) {
        Row { Text("Info Dialog") }
    }
}