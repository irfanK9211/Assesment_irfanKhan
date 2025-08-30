package com.fanawarit.irfankhan_assesment.domain.repository

import com.fanawarit.irfankhan_assesment.data.remote.json.JsonPlaceholderApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providePostRepository(api: JsonPlaceholderApi): PostRepository =
        PostRepositoryImpl(api)
}