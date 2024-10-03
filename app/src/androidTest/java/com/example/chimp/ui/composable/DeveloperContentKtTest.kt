package com.example.chimp.ui.composable

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import com.example.chimp.R
import com.example.chimp.model.about.About
import com.example.chimp.model.about.Email
import com.example.chimp.model.about.SocialMedia
import org.junit.Rule
import org.junit.Test
import java.net.URL

class DeveloperContentKtTest {
    @get:Rule
    val rule = createComposeRule()

    private val dev = About(
        name = "Arthur Oliveira",
        email = Email("A50543@alunos.isel.pt"),
        socialMedia = SocialMedia(
            gitHub = URL("https://github.com/Thuzys"),
            linkedIn = URL("https://www.linkedin.com/in/arthur-cesar-oliveira-681643184/")
        ),
        imageId = R.drawable.thuzy_profile_pic
    )

    @Test
    fun `developer content shows developer content`() {
        rule.setContent { DeveloperContent(dev = dev) }
        rule.onNodeWithTag(DEVELOPER_CONTENT_CONTAINER_TAG).assertIsDisplayed()
    }

    @Test
    fun `developer content shows developer image`() {
        rule.setContent { DeveloperContent(dev = dev) }
        rule.onNodeWithTag(DEVELOPER_CONTENT_IMAGE_TAG, useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun `developer content shows developer name`() {
        rule.setContent { DeveloperContent(dev = dev) }
        rule.onNodeWithTag(DEVELOPER_CONTENT_NAME_TAG, useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun `developer content shows developer bio`() {
        rule.setContent { DeveloperContent(dev = dev) }
        rule.onNodeWithTag(DEVELOPER_CONTENT_BIO_TAG).assertIsDisplayed()
    }

    @Test
    fun `developer content shows developer social media`() {
        rule.setContent { DeveloperContent(dev = dev) }
        rule
            .onAllNodesWithTag(DEVELOPER_CONTENT_SOCIAL_MEDIA_TAG, useUnmergedTree = true)
            .onFirst()
            .assertIsDisplayed()
    }

    @Test
    fun `developer content shows developer email`() {
        rule.setContent { DeveloperContent(dev = dev) }
        rule.onNodeWithTag(DEVELOPER_CONTENT_EMAIL_TAG).assertIsDisplayed()
    }

    @Test
    fun `developer content shows developer complete bio`() {
        rule.setContent { DeveloperContent(dev = dev, showDialog = true) }
        rule.onNodeWithTag(DEVELOPER_CONTENT_COMPLETE_BIO_TAG, useUnmergedTree = true)
            .assertIsDisplayed()
    }
}