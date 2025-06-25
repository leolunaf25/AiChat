package com.lunatcoms.aichat.di

import com.lunatcoms.aichat.data.repository.ChatRepository
import com.lunatcoms.aichat.data.repository.OpenAIRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindChatRepository(
        openAIRepository: OpenAIRepository
    ): ChatRepository
} 