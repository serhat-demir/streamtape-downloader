package com.serhatd.streamtapedownloader.ui.helper

import android.content.Context
import android.widget.Toast
import com.serhatd.streamtapedownloader.R

class ToastManager(private val context: Context) {
    fun showMessage(toastMessage: ToastMessage, message: String = "") {
        val msg = when(toastMessage) {
            ToastMessage.VIDEO_URL_EMPTY -> context.resources.getString(R.string.msg_video_url_empty)
            ToastMessage.VIDEO_URL_INVALID -> context.resources.getString(R.string.msg_video_url_invalid)
            ToastMessage.VIDEO_READY_TO_DOWNLOAD -> context.resources.getString(R.string.video_is_ready_to_download)
            ToastMessage.SOMETHING_WENT_WRONG -> context.resources.getString(R.string.msg_something_went_wrong)
            ToastMessage.SERVER_DOWN -> context.resources.getString(R.string.msg_server_down)
        }

        if (message.isEmpty()) Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        else Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}