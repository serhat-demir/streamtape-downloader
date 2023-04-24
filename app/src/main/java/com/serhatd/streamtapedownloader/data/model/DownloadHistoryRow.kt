package com.serhatd.streamtapedownloader.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "download_history")
data class DownloadHistoryRow(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    var id: Int,

    @ColumnInfo("title")
    var title: String,

    @ColumnInfo("video_url")
    var video_url: String,

    @ColumnInfo("download_url")
    var download_url: String
)