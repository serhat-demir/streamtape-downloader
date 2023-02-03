package com.serhatd.streamtapedownloader.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.serhatd.streamtapedownloader.data.repository.StreamtapeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo: StreamtapeRepository): ViewModel() {
    val downloadLink = repo.downloadLink
    val toastObserver = repo.toastObserver
    val progressVisible = repo.progressVisible
    val progressMessage = repo.progressMessage

    fun getDownloadTicket(file: String) {
        repo.getDownloadTicket(file)
    }

    fun setCredentials(login: String, key: String) {
        repo.setCredentials(login, key)
    }
}