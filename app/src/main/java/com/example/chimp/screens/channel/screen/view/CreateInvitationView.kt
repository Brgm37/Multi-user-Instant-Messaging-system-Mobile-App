package com.example.chimp.screens.channel.screen.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chimp.models.channel.ChannelInvitation
import com.example.chimp.models.time.getMillisFromTimeString
import com.example.chimp.models.time.toOptionFormat
import com.example.chimp.screens.channel.model.accessControl.AccessControl
import com.example.chimp.screens.ui.composable.SelectOutlinedTextField
import java.sql.Timestamp

@Composable
internal fun ChannelInvitationView(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onGenerateClick: (ChannelInvitation) -> Unit = { },
) {
    val channelInvitation = remember { mutableStateOf(ChannelInvitation.createDefault()) }

    Column(
        modifier = modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Channel Invite Code Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                modifier = Modifier.clickable(onClick = onBackClick),
                imageVector = Icons.Default.Close,
                contentDescription = "Back",
            )
        }
        SelectOutlinedTextField(
            options = listOf("30 minutes", "1 hour", "1 day", "1 week"),
            selectedOption = channelInvitation.value.expiresAfter.toOptionFormat(),
            onOptionSelected =
            {
                channelInvitation.value =
                    channelInvitation.value.copy(expiresAfter = Timestamp(getMillisFromTimeString(it)))
            },
            label = "Expire After",
            modifier = Modifier.fillMaxWidth()
        )

        SelectOutlinedTextField(
            options = listOf("1", "2", "3", "4", "5", "10", "20", "50", "100"),
            selectedOption = channelInvitation.value.maxUses.toString(),
            onOptionSelected = { maxUses ->
                maxUses.toUIntOrNull()
                    ?.let { channelInvitation.value = channelInvitation.value.copy(maxUses = it) }
            },
            label = "Max Number of Uses",
            modifier = Modifier.fillMaxWidth()
        )

        SelectOutlinedTextField(
            options = listOf("READ_WRITE", "READ_ONLY"),
            selectedOption = channelInvitation.value.permission.name,
            onOptionSelected = {
                channelInvitation.value =
                    channelInvitation.value.copy(permission = AccessControl.valueOf(it))
            },
            label = "Permissions",
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Warning: Notice that previous invitations created will be deleted.",
            color = Color.Red
        )
        Text(
            text = "Suggestion: If you want to invite multiple users, increment maxUses instead of creating multiple invitation codes."
        )
        Button(
            onClick = { onGenerateClick(channelInvitation.value) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate a New Invitation Code")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChannelInvitationViewPreview() {
    ChannelInvitationView(onGenerateClick = {})
}