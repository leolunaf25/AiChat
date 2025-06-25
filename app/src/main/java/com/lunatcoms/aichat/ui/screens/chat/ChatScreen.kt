package com.lunatcoms.aichat.ui.screens.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.lunatcoms.aichat.ui.theme.AiChatTheme
import com.lunatcoms.aichat.ui.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Pantalla principal del chat
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Mostrar SnackBar cuando hay un error
    LaunchedEffect(uiState.error) {
        uiState.error?.let { errorMessage ->
            snackbarHostState.showSnackbar(message = errorMessage)
        }
    }
    
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("AI Chat") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            ChatInputBar(
                onSendMessage = { message ->
                    viewModel.sendMessage(message)
                },
                isLoading = uiState.isLoading
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { snackbarData ->
                    Snackbar(
                        snackbarData = snackbarData,
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            )
        }
    ) { paddingValues ->
        // Contenido principal del chat
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (uiState.messages.isEmpty()) {
                EmptyChatScreen()
            } else {
                ChatMessagesList(
                    messages = uiState.messages,
                    isLoading = uiState.isLoading
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    AiChatTheme {
        ChatScreen()
    }
} 