package com.lunatcoms.aichat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lunatcoms.aichat.ui.screens.chat.ChatScreen
import com.lunatcoms.aichat.ui.theme.AiChatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AiChatTheme {
                ChatScreen()
            }
        }
    }
}