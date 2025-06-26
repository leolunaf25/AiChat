package com.lunatcoms.aichat.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lunatcoms.aichat.BuildConfig
import com.lunatcoms.aichat.data.model.ChatMessage
import com.lunatcoms.aichat.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de chat
 * Maneja la lógica de negocio relacionada con el chat
 */
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    // Estado UI
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    // Verificar si la API key está configurada
    private val isApiKeyConfigured = BuildConfig.OPENAI_API_KEY.isNotBlank()

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
                isLoading = true, // Activar indicador de carga
                error = null // Limpiar errores anteriores
            )
        }
        
        // Verificar si podemos usar la API
        if (!isApiKeyConfigured) {
            handleApiUnavailable()
            return
        }
        
        // Enviar el mensaje a la API de OpenAI y procesar la respuesta
        viewModelScope.launch {
            try {
                val currentMessages = _uiState.value.messages
                
                // Llamar al repositorio para obtener la respuesta de la IA
                val result = chatRepository.sendMessage(
                    message = content,
                    conversationHistory = currentMessages.dropLast(1) // Excluir el mensaje que acabamos de añadir
                )
                
                // Procesar el resultado
                result.fold(
                    onSuccess = { aiMessage ->
                        // Añadir la respuesta de la IA a la conversación
                        _uiState.update { currentState ->
                            currentState.copy(
                                messages = currentState.messages + aiMessage,
                                isLoading = false
                            )
                        }
                    },
                    onFailure = { error ->
                        handleApiError(error)
                    }
                )
            } catch (e: Exception) {
                handleApiError(e)
            }
        }
    }
    
    /**
     * Maneja el caso en que la API no está disponible (no hay API key)
     */
    private fun handleApiUnavailable() {
        val fallbackMessage = ChatMessage(
            content = "Por el momento no hay chat disponible, inténtelo más tarde.",
            isFromUser = false
        )
        
        _uiState.update { currentState ->
            currentState.copy(
                messages = currentState.messages + fallbackMessage,
                isLoading = false
            )
        }
    }
    
    /**
     * Maneja errores de la API
     */
    private fun handleApiError(error: Throwable) {
        val errorMessage = ChatMessage(
            content = "Por el momento no hay chat disponible, inténtelo más tarde.",
            isFromUser = false
        )
        
        _uiState.update { currentState ->
            currentState.copy(
                messages = currentState.messages + errorMessage,
                isLoading = false,
                error = "Error de comunicación: ${error.message ?: "Error desconocido"}"
            )
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