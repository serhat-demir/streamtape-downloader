package com.serhatd.streamtapedownloader.data.model

import com.google.gson.annotations.SerializedName

data class TicketResponse(
    @SerializedName("status")
    var status: Int,

    @SerializedName("msg")
    var msg: String,

    @SerializedName("result")
    var result: DownloadTicket
)