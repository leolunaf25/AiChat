package com.lunatcoms.aichat.ui.screens.chat

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.graphics.Color
import com.lunatcoms.aichat.ui.theme.AiChatTheme

/**
 * Barra de entrada de texto para el chat
 * 
 * @param onSendMessage Callback que se ejecuta cuando se envía un mensaje
 * @param isLoading Indica si la IA está procesando una respuesta
 * @param modifier Modifier para personalizar el componente
 */
@Composable
fun ChatInputBar(
    onSendMessage: (String) -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    var userInput by remember { mutableStateOf("") }
    val isInputEnabled = !isLoading && userInput.isNotBlank()
    
    BottomAppBar(
        modifier = modifier
            .navigationBarsPadding()
            .imePadding(),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        OutlinedTextField(
            value = userInput,
            onValueChange = { userInput = it },
            modifier = Modifier
                .weight(1f)
                .padding(8.dp, 8.dp, 8.dp, 8.dp),
            placeholder = { Text("Escribe un mensaje...") },
            shape = RoundedCornerShape(24.dp),
            maxLines = 5,
            enabled = true
        )
        
        FloatingActionButton(
            onClick = {
                if (userInput.isNotBlank() && !isLoading) {
                    onSendMessage(userInput)
                    userInput = ""
                }
            },
            modifier = Modifier
                .padding(end = 8.dp)
                .align(Alignment.CenterVertically),
            shape = CircleShape,
            containerColor = if (isInputEnabled) 
                MaterialTheme.colorScheme.primary 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Enviar mensaje",
                tint = if (isInputEnabled) 
                    MaterialTheme.colorScheme.onPrimary 
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}

@Preview
@Composable
fun ChatInputBarPreview() {
    AiChatTheme {
        ChatInputBar(onSendMessage = {})
    }
}

@Preview
@Composable
fun ChatInputBarLoadingPreview() {
    AiChatTheme {
        ChatInputBar(onSendMessage = {}, isLoading = true)
    }
} 