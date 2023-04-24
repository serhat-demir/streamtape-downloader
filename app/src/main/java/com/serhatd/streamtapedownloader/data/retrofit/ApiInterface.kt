package com.serhatd.streamtapedownloader.data.retrofit

import com.serhatd.streamtapedownloader.data.model.DownloadResponse
import com.serhatd.streamtapedownloader.data.model.TicketResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("dlticket")
    suspend fun getDownloadTicket(
        @Query("file") file: String,
        @Query("login") login: String,
        @Query("key") key: String
    ): Response<TicketResponse>

    @GET("dl")
    suspend fun downloadFile(
        @Query("file") file: String,
        @Query("ticket") ticket: String
    ): Response<DownloadResponse>
}