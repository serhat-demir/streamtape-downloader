package com.serhatd.streamtapedownloader.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.serhatd.streamtapedownloader.data.model.DownloadHistoryRow

@Dao
interface DownloadHistoryDao {
    @Query("SELECT * FROM download_history ORDER BY id DESC")
    suspend fun getDownloadHistory(): List<DownloadHistoryRow>

    @Insert
    suspend fun addDownloadHistory(downloadHistoryRow: DownloadHistoryRow)

    @Query("DELETE FROM download_history")
    suspend fun deleteDownloadHistory()
}