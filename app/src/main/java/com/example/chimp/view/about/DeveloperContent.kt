package com.example.chimp.view.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.example.chimp.R

@Composable
fun DeveloperContent(
    modifier: Modifier = Modifier,
    devId: Int,
    devName: String,
    devGitHub: String/*TODO:See the possibility of using url(data class)*/,
    devEmail: String /*TODO: See the possibility of using email(data class)*/,
    gitOnClick: (String) -> Unit = {},
    emailOnClick: (String) -> Unit = {},
    onClick: () -> Unit = {}
) {
    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        //TODO: Put the image and text in a Column to increase the onClick area
        Image(
            painter = painterResource(id = devId),
            contentDescription = "Developer Profile Picture",
            modifier =
                modifier
                    .size(200.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.extraLarge)
                    .clickable(onClick = onClick)
        )
        Text(
            text = devName,
            modifier =
            modifier
                .fillMaxWidth()
                .padding(16.dp),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
        )
        HorizontalDivider()
        //TODO: Increase the clickable area
        Image(
            painter = painterResource(R.drawable.github_mark),
            contentDescription = "GitHub Logo",
            modifier = Modifier
                .size(40.dp)
                .padding(4.dp)
                /*TODO: How to open the chrome */
                .clickable(onClick = { gitOnClick(devGitHub) })
        )
        HorizontalDivider()
        Image(
            painter = painterResource(R.drawable.email_light_mode),
            contentDescription = "Email Logo",
            modifier = Modifier
                .size(40.dp)
                .padding(4.dp)
                /*TODO: How to send an email */
                .clickable(onClick = { emailOnClick(devEmail) })
        )
    }
}

private data class DevInfo(
    val devId: Int,
    val devName: String,
    val devGitHub: String,
    val devEmail: String,
)

private class DeveloperContentPreviewClass : PreviewParameterProvider<DevInfo> {
    override val values: Sequence<DevInfo> = sequenceOf(
        DevInfo(
            devId = R.drawable.thuzy_profile_pic,
            devName = "Thuzy",
            devGitHub = "test",
            devEmail = "test"
        )
    )
}


@Composable
@Preview(showBackground = true)
private fun DeveloperContendPreview(
    @PreviewParameter(DeveloperContentPreviewClass::class) devInfo: DevInfo
) {
    DeveloperContent(
        devId = devInfo.devId,
        devName = devInfo.devName,
        devGitHub = devInfo.devGitHub,
        devEmail = devInfo.devEmail,
    )
}