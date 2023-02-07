package com.serhatd.streamtapedownloader.data.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        private const val BASE_URL = "https://api.streamtape.com/file/"
        public const val API_USER = "6449fe61adb4f182b648"
        public const val API_PASS = "xraLO3PJKRtkGyx"

        fun getApiService(): ApiInterface {
            val client = OkHttpClient.Builder()
            client.addInterceptor { chain ->
                val req = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36")
                    .build()
                chain.proceed(req)
            }

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client.build())
                .build()
                .create(ApiInterface::class.java)
        }
    }
}