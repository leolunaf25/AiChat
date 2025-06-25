package com.lunatcoms.aichat.data.model

import java.util.UUID

/**
 * Representa un mensaje en la conversación de chat
 * 
 * @property id Identificador único del mensaje
 * @property content Contenido del mensaje
 * @property isFromUser Indica si el mensaje fue enviado por el usuario (true) o por la IA (false)
 * @property timestamp Momento en que se envió el mensaje
 */
data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
) 