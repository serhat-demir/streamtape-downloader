package com.serhatd.streamtapedownloader.data.retrofit

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        private const val BASE_URL = "https://api.streamtape.com/file/"
        const val API_USER = "6449fe61adb4f182b648"
        const val API_PASS = "xraLO3PJKRtkGyx"

        fun getApiService(): ApiInterface {
            val client = OkHttpClient.Builder()
            client.addInterceptor { chain ->
                val req = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent", "Mozilla/5.0 (Linux x86_64) Gecko/20100101 Firefox/61.4")
                    .build()
                chain.proceed(req)
            }

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(client.build())
                .build()
                .create(ApiInterface::class.java)
        }
    }
}