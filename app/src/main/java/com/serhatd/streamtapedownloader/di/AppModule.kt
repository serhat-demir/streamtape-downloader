package com.serhatd.streamtapedownloader.di

import android.content.Context
import com.serhatd.streamtapedownloader.data.repository.StreamtapeRepository
import com.serhatd.streamtapedownloader.data.retrofit.ApiClient
import com.serhatd.streamtapedownloader.data.retrofit.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideStreamtapeRepository(apiService: ApiInterface, @ApplicationContext context: Context): StreamtapeRepository = StreamtapeRepository(apiService, context)

    @Singleton
    @Provides
    fun provideApiService(): ApiInterface = ApiClient.getApiService()
}