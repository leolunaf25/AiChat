package com.lunatcoms.aichat.data.repository

import com.lunatcoms.aichat.data.model.ChatMessage

/**
 * Interfaz que define los métodos para interactuar con la API de chat
 */
interface ChatRepository {
    
    /**
     * Envía un mensaje a la API de chat y devuelve la respuesta
     * 
     * @param message Mensaje a enviar
     * @param conversationHistory Lista de mensajes previos para proporcionar contexto
     * @return Mensaje de respuesta generado por la IA
     */
    suspend fun sendMessage(message: String, conversationHistory: List<ChatMessage>): Result<ChatMessage>
} 