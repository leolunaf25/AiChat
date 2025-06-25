package com.lunatcoms.aichat.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lunatcoms.aichat.ui.theme.AiChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    var userInput by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Chat") }
//                ,
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
//                )
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
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
                    maxLines = 5
                )

                FloatingActionButton(
                    onClick = {
                        // Aquí iría la lógica para enviar el mensaje
                        if (userInput.isNotBlank()) {
                            userInput = ""
                        }
                    },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .align(Alignment.CenterVertically),
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Enviar mensaje",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
    ) { paddingValues ->
        // Aquí irá el contenido principal del chat (mensajes)
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Este espacio se llenará posteriormente con los mensajes
            Text(
                text = "Empieza a chatear con la IA",
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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