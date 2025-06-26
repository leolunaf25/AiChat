package com.lunatcoms.aichat.data.repository

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage as OpenAIChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.lunatcoms.aichat.BuildConfig
import com.lunatcoms.aichat.data.model.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OpenAIRepository @Inject constructor() : ChatRepository {
    
    // Crear el cliente de OpenAI con la API key solo si está configurada
    private val openAI by lazy {
        if (BuildConfig.OPENAI_API_KEY.isNotBlank()) {
            OpenAI(token = BuildConfig.OPENAI_API_KEY)
        } else {
            null
        }
    }
    
    // Modelo a utilizar
    private val model = ModelId("gpt-3.5-turbo")
    
    // Tiempo máximo de espera para la respuesta de la API (en milisegundos)
    private val timeoutMs = 30_000L
    
    override suspend fun sendMessage(
        message: String,
        conversationHistory: List<ChatMessage>
    ): Result<ChatMessage> = withContext(Dispatchers.IO) {
        try {
            // Verificar si la API key está configurada
            val api = openAI ?: return@withContext Result.failure(
                Exception("API de OpenAI no configurada")
            )
            
            // Convertir el historial de mensajes al formato esperado por la API de OpenAI
            val messages = mutableListOf<OpenAIChatMessage>()
            
            // Añadir mensaje del sistema para dar contexto
            messages.add(
                OpenAIChatMessage(
                    role = ChatRole.System,
                    content = "Eres un asistente útil y amigable que ayuda a los usuarios de una aplicación de chat."
                )
            )
            
            // Añadir el historial de conversación
            conversationHistory.forEach { chatMessage ->
                val role = if (chatMessage.isFromUser) ChatRole.User else ChatRole.Assistant
                messages.add(
                    OpenAIChatMessage(
                        role = role,
                        content = chatMessage.content
                    )
                )
            }
            
            // Añadir el mensaje actual
            messages.add(
                OpenAIChatMessage(
                    role = ChatRole.User,
                    content = message
                )
            )
            
            // Crear la solicitud de chat completion
            val completionRequest = ChatCompletionRequest(
                model = model,
                messages = messages
            )
            
            // Enviar la solicitud y obtener la respuesta con un timeout
            val completion = withTimeout(timeoutMs) {
                api.chatCompletion(completionRequest)
            }
            
            // Extraer el contenido de la respuesta
            val responseContent = completion.choices.firstOrNull()?.message?.content
                ?: return@withContext Result.failure(Exception("No se recibió respuesta de la IA"))
            
            // Crear y devolver el mensaje de respuesta
            val aiMessage = ChatMessage(
                content = responseContent,
                isFromUser = false
            )
            
            Result.success(aiMessage)
        } catch (e: TimeoutCancellationException) {
            Result.failure(Exception("Tiempo de espera agotado al comunicarse con la IA"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 