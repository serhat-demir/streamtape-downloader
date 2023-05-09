package com.serhatd.streamtapedownloader.ui.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serhatd.streamtapedownloader.data.model.DownloadHistoryRow
import com.serhatd.streamtapedownloader.data.repository.StreamtapeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val repo: StreamtapeRepository): ViewModel() {
    val downloadHistory = MutableLiveData<List<DownloadHistoryRow>>()

    fun getDownloadHistory() {
        viewModelScope.launch {
            downloadHistory.value = repo.getDownloadHistory()
        }
    }

    fun deleteDownloadHistoryRow(id: Int) {
        viewModelScope.launch {
            repo.deleteDownloadHistoryRow(id)
        }

        getDownloadHistory()
    }

    fun deleteDownloadHistory() {
        viewModelScope.launch {
            repo.deleteDownloadHistory()
        }

        getDownloadHistory()
    }
}