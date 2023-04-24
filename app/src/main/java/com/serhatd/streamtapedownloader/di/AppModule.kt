package com.serhatd.streamtapedownloader.di

import android.content.Context
import androidx.room.Room
import com.serhatd.streamtapedownloader.data.repository.StreamtapeRepository
import com.serhatd.streamtapedownloader.data.retrofit.ApiClient
import com.serhatd.streamtapedownloader.data.retrofit.ApiInterface
import com.serhatd.streamtapedownloader.data.room.DB
import com.serhatd.streamtapedownloader.data.room.DownloadHistoryDao
import com.serhatd.streamtapedownloader.ui.helper.ToastManager
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
    fun provideStreamtapeRepository(apiService: ApiInterface, downloadHistoryDao: DownloadHistoryDao): StreamtapeRepository = StreamtapeRepository(apiService, downloadHistoryDao)

    @Singleton
    @Provides
    fun provideApiService(): ApiInterface = ApiClient.getApiService()

    @Singleton
    @Provides
    fun provideDownloadHistoryDao(@ApplicationContext context: Context): DownloadHistoryDao {
        val db = Room.databaseBuilder(context, DB::class.java, "streamtapedownloader.sqlite")
            .createFromAsset("streamtapedownloader.sqlite").build()

        return db.getDownloadHistoryDao()
    }

    @Singleton
    @Provides
    fun provideApiCallback(@ApplicationContext context: Context) = ToastManager(context)
}