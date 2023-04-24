package com.serhatd.streamtapedownloader.ui.history

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.serhatd.streamtapedownloader.R
import com.serhatd.streamtapedownloader.data.model.DownloadHistoryRow
import com.serhatd.streamtapedownloader.databinding.CardDownloadHistoryRowBinding
import com.serhatd.streamtapedownloader.ui.helper.ConnectionManager

class HistoryAdapter(private val context: Context, private val downloadManager: DownloadManager, private val downloadHistory: List<DownloadHistoryRow>): RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
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

    fun openVideo(url: String) {
        if (!ConnectionManager.checkInternetConnection(context)) return

        var newUrl = url
        if (!url.startsWith(context.getString(R.string.http_prefix)) && !url.startsWith(context.getString(R.string.https_prefix)))
            newUrl = context.getString(R.string.http_prefix) + url

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newUrl))
        context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return downloadHistory.size
    }
}