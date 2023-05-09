package com.serhatd.streamtapedownloader.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.serhatd.streamtapedownloader.data.model.DownloadHistoryRow

@Database(entities = [DownloadHistoryRow::class], version = 1)
abstract class DB: RoomDatabase() {
    abstract fun getDownloadHistoryDao(): DownloadHistoryDao
}