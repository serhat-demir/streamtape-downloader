package com.serhatd.streamtapedownloader.data.model

import com.google.gson.annotations.SerializedName

data class DownloadTicket(
    @SerializedName("ticket")
    var ticket: String,

    @SerializedName("wait_time")
    var wait_time: Long,

    @SerializedName("valid_until")
    var valid_until: String
)