package com.serhatd.streamtapedownloader.ui.download

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serhatd.streamtapedownloader.data.model.DownloadHistoryRow
import com.serhatd.streamtapedownloader.data.model.DownloadLink
import com.serhatd.streamtapedownloader.data.repository.StreamtapeRepository
import com.serhatd.streamtapedownloader.ui.helper.ToastManager
import com.serhatd.streamtapedownloader.ui.helper.ToastMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(private val repo: StreamtapeRepository, private val toast: ToastManager): ViewModel() {
    val downloadLink = MutableLiveData<DownloadLink>()
    val progressVisible = MutableLiveData<Boolean>()
    val progressStatus = MutableLiveData<Int>()

    private fun getVideoId(url: String): String {
        val pattern = "(?:streamtape\\.com\\/v\\/)([\\w\\-]+)\\/.+".toRegex()
        val matchResult = pattern.find(url)
        return matchResult?.groupValues?.getOrNull(1) ?: ""
    }

    fun getDownloadTicket(video_url: String) {
        if (video_url.trim().isEmpty()) {
            toast.showMessage(ToastMessage.VIDEO_URL_EMPTY)
            return
        }

        // extract id from url
        val file_id = getVideoId(video_url)
        if (file_id.isEmpty()) {
            toast.showMessage(ToastMessage.VIDEO_URL_INVALID)
            return
        }

        viewModelScope.launch {
            val response = repo.getDownloadTicket(file_id)

            if (response.isSuccessful && response.body() != null && response.body()!!.result != null) {
                val waitTime = response.body()!!.result.wait_time
                var totalTime = 0

                progressStatus.value = 0
                progressVisible.value = true
                val timer = object: CountDownTimer(waitTime * 1000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        totalTime += 1000
                        progressStatus.value = (((totalTime / 1000) * 100) / waitTime).toInt()
                    }

                    override fun onFinish() {
                        downloadFile(file_id, response.body()!!.result.ticket, video_url)
                    }
                }

                timer.start()
            } else toast.showMessage(ToastMessage.SERVER_DOWN)
        }
    }

    private fun downloadFile(file_id: String, ticket: String, video_url: String) {
        viewModelScope.launch {
            val response = repo.downloadFile(file_id, ticket)

            if (response.isSuccessful && response.body() != null) {
                progressVisible.value = false
                downloadLink.value = response.body()!!.result

                addDownloadHistory(downloadLink.value!!.name, video_url, downloadLink.value!!.url)
                toast.showMessage(ToastMessage.VIDEO_READY_TO_DOWNLOAD)
            } else toast.showMessage(ToastMessage.SOMETHING_WENT_WRONG)
        }
    }

    private fun addDownloadHistory(title: String, video_url: String, download_url: String) {
        val downloadHistoryRow = DownloadHistoryRow(0, title, video_url, download_url)
        viewModelScope.launch {
            repo.addDownloadHistory(downloadHistoryRow)
        }
    }
}