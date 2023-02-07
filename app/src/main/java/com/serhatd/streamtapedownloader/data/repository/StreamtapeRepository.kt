package com.serhatd.streamtapedownloader.data.repository

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import com.serhatd.streamtapedownloader.R
import com.serhatd.streamtapedownloader.data.model.DownloadLink
import com.serhatd.streamtapedownloader.data.model.DownloadResponse
import com.serhatd.streamtapedownloader.data.model.TicketResponse
import com.serhatd.streamtapedownloader.data.retrofit.ApiClient
import com.serhatd.streamtapedownloader.data.retrofit.ApiInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class StreamtapeRepository(private val apiService: ApiInterface, private val context: Context) {
    private val disposable = CompositeDisposable()
    val downloadLink = MutableLiveData<DownloadLink>()
    val toastObserver = MutableLiveData<String>()
    val progressVisible = MutableLiveData<Boolean>()
    val progressMessage = MutableLiveData<String>()

    fun getDownloadTicket(file: String) {
        val login = ApiClient.API_USER
        val key = ApiClient.API_PASS

        if (file.trim().isEmpty()) {
            toastObserver.value = context.resources.getString(R.string.msg_file_id_error)
            return
        }

        progressVisible.value = true
        disposable.add(
            apiService.getDownloadTicket(file, login, key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<TicketResponse>() {
                    override fun onSuccess(t: TicketResponse) {
                        val waitTime = t.result.wait_time
                        var totalTime = 0
                        var statusPercent: Int

                        val timer = object: CountDownTimer(waitTime * 1000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                totalTime += 1000
                                statusPercent = (((totalTime / 1000) * 100) / waitTime).toInt()

                                progressMessage.value = context.resources.getString(R.string.msg_video_preparing) + " $statusPercent%"
                            }

                            override fun onFinish() {
                                downloadFile(file, t.result.ticket)
                                progressMessage.value = context.resources.getString(R.string.msg_download_url_generating)
                            }
                        }

                        timer.start()
                    }

                    override fun onError(e: Throwable) {
                        toastObserver.value = context.resources.getString(R.string.msg_error)
                        progressVisible.value = false
                    }
                })
        )
    }

    private fun downloadFile(file: String, ticket: String) {
        disposable.add(
            apiService.downloadFile(file, ticket)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<DownloadResponse>() {
                    override fun onSuccess(t: DownloadResponse) {
                        progressVisible.value = false
                        toastObserver.value = context.resources.getString(R.string.msg_video_ready)

                        downloadLink.value = t.result
                    }

                    override fun onError(e: Throwable) {
                        progressVisible.value = false
                        toastObserver.value = context.resources.getString(R.string.msg_error)
                    }
                })
        )
    }
}