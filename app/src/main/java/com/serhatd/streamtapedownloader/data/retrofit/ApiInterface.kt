package com.serhatd.streamtapedownloader.data.retrofit

import com.serhatd.streamtapedownloader.data.model.DownloadResponse
import com.serhatd.streamtapedownloader.data.model.TicketResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("dlticket")
    fun getDownloadTicket(
        @Query("file") file: String,
        @Query("login") login: String,
        @Query("key") key: String
    ): Single<TicketResponse>

    @GET("dl")
    fun downloadFile(
        @Query("file") file: String,
        @Query("ticket") ticket: String
    ): Single<DownloadResponse>
}