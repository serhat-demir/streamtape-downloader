package com.serhatd.streamtapedownloader.ui.history

import android.app.DownloadManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.serhatd.streamtapedownloader.MainActivity
import com.serhatd.streamtapedownloader.R
import com.serhatd.streamtapedownloader.data.model.DownloadHistoryRow
import com.serhatd.streamtapedownloader.databinding.CardDownloadHistoryRowBinding
import com.serhatd.streamtapedownloader.ui.helper.ConnectionManager

class HistoryAdapter(private val context: Context, private val activity: MainActivity, private val viewModel: HistoryViewModel, private val downloadManager: DownloadManager, private val downloadHistory: List<DownloadHistoryRow>): RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
   class HistoryViewHolder(val binding: CardDownloadHistoryRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding: CardDownloadHistoryRowBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.card_download_history_row, parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val downloadHistoryRow = downloadHistory[position]
        holder.binding.downloadHistoryRow = downloadHistoryRow
        holder.binding.adapter = this
    }

    fun downloadVideo(url: String) {
        if (!ConnectionManager.checkInternetConnection(context)) return

        val uri = Uri.parse(url)
        val request = DownloadManager.Request(uri)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        downloadManager.enqueue(request)
    }

    fun deleteDownloadHistoryRow(view: View, id: Int) {
        Snackbar.make(view, context.getString(R.string.msg_delete_history_row), Snackbar.LENGTH_LONG).setAction(context.getString(R.string.btn_yes)) {
            viewModel.deleteDownloadHistoryRow(id)
        }.show()
    }

    fun openUrl(url: String) {
        if (!ConnectionManager.checkInternetConnection(context)) return

        var newUrl = url
        if (!url.startsWith(context.getString(R.string.http_prefix)) && !url.startsWith(context.getString(R.string.https_prefix)))
            newUrl = context.getString(R.string.http_prefix) + url

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newUrl))
        context.startActivity(intent)
    }

    fun copyUrl(url: String) {
        val clipboardManager = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("", url)
        clipboardManager.setPrimaryClip(clipData)

        Toast.makeText(context, context.getString(R.string.link_copied), Toast.LENGTH_LONG).show()
    }

    override fun getItemCount(): Int {
        return downloadHistory.size
    }
}