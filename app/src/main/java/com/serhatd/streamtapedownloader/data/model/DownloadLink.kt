package com.serhatd.streamtapedownloader.data.model

import com.google.gson.annotations.SerializedName

data class DownloadLink(
    @SerializedName("name")
    var name: String,

    @SerializedName("size")
    var size: Int,

    @SerializedName("url")
    var url: String
)