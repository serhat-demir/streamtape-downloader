package com.serhatd.streamtapedownloader.data.repository

import com.serhatd.streamtapedownloader.data.model.DownloadHistoryRow
import com.serhatd.streamtapedownloader.data.model.DownloadResponse
import com.serhatd.streamtapedownloader.data.model.TicketResponse
import com.serhatd.streamtapedownloader.data.retrofit.ApiClient
import com.serhatd.streamtapedownloader.data.retrofit.ApiInterface
import com.serhatd.streamtapedownloader.data.room.DownloadHistoryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class StreamtapeRepository(private val apiService: ApiInterface, private val downloadHistoryDao: DownloadHistoryDao) {
    suspend fun getDownloadTicket(file_id: String): Response<TicketResponse> {
        val login = ApiClient.API_USER
        val key = ApiClient.API_PASS

        return withContext(Dispatchers.IO) {
            apiService.getDownloadTicket(file_id, login, key)
        }
    }

    suspend fun downloadFile(file_id: String, ticket: String): Response<DownloadResponse> {
        return withContext(Dispatchers.IO) {
            apiService.downloadFile(file_id, ticket)
        }
    }

    suspend fun getDownloadHistory(): List<DownloadHistoryRow> {
        return withContext(Dispatchers.IO) {
            downloadHistoryDao.getDownloadHistory()
        }
    }

    suspend fun addDownloadHistory(downloadHistoryRow: DownloadHistoryRow) {
        downloadHistoryDao.addDownloadHistory(downloadHistoryRow)
    }

    suspend fun deleteDownloadHistory() {
        downloadHistoryDao.deleteDownloadHistory()
    }
}