package com.lunatcoms.aichat.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lunatcoms.aichat.data.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de chat
 * Maneja la lógica de negocio relacionada con el chat
 */
class ChatViewModel : ViewModel() {

    // Estado UI
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    /**
     * Envía un mensaje del usuario a la conversación
     * @param content Contenido del mensaje
     */
    fun sendMessage(content: String) {
        if (content.isBlank()) return
        
        val userMessage = ChatMessage(
            content = content,
            isFromUser = true
        )
        
        // Añadir el mensaje del usuario a la lista de mensajes
        _uiState.update { currentState ->
            currentState.copy(
                messages = currentState.messages + userMessage,
                isLoading = true // Activar indicador de carga
            )
        }
        
        // En una implementación real, aquí se enviaría el mensaje a la API de OpenAI
        // y se procesaría la respuesta
        
        // Por ahora, simulamos una respuesta de la IA
        simulateAiResponse(content)
    }
    
    /**
     * Simula una respuesta de la IA
     * En una implementación real, esto se reemplazaría por una llamada a la API de OpenAI
     */
    private fun simulateAiResponse(userMessage: String) {
        viewModelScope.launch {
            // Simulamos un pequeño retraso para hacer la respuesta más realista
            kotlinx.coroutines.delay(2000) // Aumentamos el retraso para ver mejor el indicador de carga
            
            val aiResponse = ChatMessage(
                content = "Has enviado: $userMessage. Esta es una respuesta simulada de la IA.",
                isFromUser = false
            )
            
            _uiState.update { currentState ->
                currentState.copy(
                    messages = currentState.messages + aiResponse,
                    isLoading = false // Desactivar indicador de carga
                )
            }
        }
    }
}

/**
 * Estado de la UI para la pantalla de chat
 */
data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 