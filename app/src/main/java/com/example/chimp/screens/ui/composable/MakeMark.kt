package com.example.chimp.screens.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.chimp.R

const val MAKE_MARK_TAG = "Make Mark"

/**
 * The composable function that displays the mark.
 * @param modifier [Modifier] The modifier to be applied to the layout.
 * @param lightMode [Int] The light mode image resource id.
 * @param darkMode [Int] The dark mode image resource id.
 * @param contentDescription [String] The content description of the image.
 */
@Composable
fun MakeMark(
    modifier: Modifier = Modifier,
    lightMode: Int,
    darkMode: Int = lightMode,
    contentDescription: String
) {
    if (isSystemInDarkTheme()) {
        Image(
            painter = painterResource(darkMode),
            contentDescription = contentDescription,
            modifier = modifier.testTag(MAKE_MARK_TAG)
        )
    } else {
        Image(
            painter = painterResource(lightMode),
            contentDescription = contentDescription,
            modifier = modifier.testTag(MAKE_MARK_TAG)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MakeMarkPreview() {
    MakeMark(
        lightMode = R.drawable.github_mark,
        darkMode = R.drawable.github_mark_white,
        contentDescription = "GitHub Logo"
    )
}