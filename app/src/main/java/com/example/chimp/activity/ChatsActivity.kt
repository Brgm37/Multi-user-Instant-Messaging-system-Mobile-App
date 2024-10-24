package com.example.chimp.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.chimp.ui.composable.MenuBottomBar
import com.example.chimp.ui.screen.ChIMPChatsScreen
import com.example.chimp.ui.theme.ChIMPTheme

class ChatsActivity : ComponentActivity() {

    private val navigateToAboutIntent: Intent by lazy {
        Intent(this, AboutActivity::class.java)
    }

    private val navigateToFindChannelIntent: Intent by lazy {
        Intent(this, FindChannelActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChIMPTheme {
                Scaffold(
                    bottomBar = {
                        MenuBottomBar(
                            menuIsEnable = false,
                            addChannelClick = { startActivity(navigateToFindChannelIntent) },
                            aboutClick = { startActivity(navigateToAboutIntent) }
                        )
                    }
                ) { innerPadding ->
                    ChIMPChatsScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

//TODO: Change email icon on aboutScreen.

