package com.lunatcoms.aichat.ui.screens.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lunatcoms.aichat.data.model.ChatMessage
import com.lunatcoms.aichat.ui.theme.AiChatTheme

/**
 * Pantalla vacía que se muestra cuando no hay mensajes
 */
@Composable
fun EmptyChatScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Empieza a chatear con la IA",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Lista de mensajes de chat
 */
@Composable
fun ChatMessagesList(
    messages: List<ChatMessage>,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    
    // Efecto para desplazar al último mensaje cuando se añade uno nuevo
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }
    
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(16.dp)
    ) {
        items(messages) { message ->
            ChatMessageItem(message = message)
        }
        
        if (isLoading) {
            item {
                LoadingIndicator()
            }
        }
    }
}

/**
 * Indicador de carga mientras la IA está procesando
 */
@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        CircularProgressIndicator(
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.secondary,
            strokeWidth = 2.dp
        )
    }
}

/**
 * Elemento individual de mensaje de chat
 */
@Composable
fun ChatMessageItem(message: ChatMessage, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = if (message.isFromUser) Alignment.End else Alignment.Start
    ) {
        Card(
            modifier = Modifier.padding(horizontal = 8.dp),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isFromUser) 16.dp else 4.dp,
                bottomEnd = if (message.isFromUser) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isFromUser) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(16.dp),
                color = if (message.isFromUser) 
                    MaterialTheme.colorScheme.onPrimary 
                else 
                    MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatMessageItemPreview() {
    AiChatTheme {
        Column {
            ChatMessageItem(
                message = ChatMessage(
                    content = "Hola, ¿cómo estás?",
                    isFromUser = true
                )
            )
            ChatMessageItem(
                message = ChatMessage(
                    content = "Estoy bien, gracias por preguntar. ¿En qué puedo ayudarte hoy?",
                    isFromUser = false
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyChatScreenPreview() {
    AiChatTheme {
        EmptyChatScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingIndicatorPreview() {
    AiChatTheme {
        LoadingIndicator()
    }
} 