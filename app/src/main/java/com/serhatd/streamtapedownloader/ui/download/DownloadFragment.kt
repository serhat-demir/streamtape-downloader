package com.serhatd.streamtapedownloader.ui.download

import android.app.DownloadManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.serhatd.streamtapedownloader.R
import com.serhatd.streamtapedownloader.databinding.FragmentDownloadBinding
import com.serhatd.streamtapedownloader.ui.helper.ConnectionManager
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import io.noties.markwon.SoftBreakAddsNewLinePlugin

@AndroidEntryPoint
class DownloadFragment : Fragment() {
    private lateinit var binding: FragmentDownloadBinding
    private lateinit var viewModel: DownloadViewModel
    private lateinit var downloadManager: DownloadManager
    private lateinit var player: ExoPlayer
    private lateinit var clipboardManager: ClipboardManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: DownloadViewModel by viewModels()
        viewModel = tempViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_download, container, false)
        binding.fragment = this

        // check internet connection
        if (!ConnectionManager.checkInternetConnection(requireContext())) return binding.root

        // init download manager
        downloadManager = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        // init clipboard manager
        clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        // receive url from browsers
        if (Intent.ACTION_SEND == requireActivity().intent.action && requireActivity().intent.type == "text/plain") {
            binding.txtVideoUrl.setText(requireActivity().intent.getStringExtra(Intent.EXTRA_TEXT))
        }

        // init player
        player = ExoPlayer.Builder(requireContext()).build()

        // init observers
        initObservers()

        return binding.root
    }

    private fun initObservers() {
        viewModel.downloadLink.observe(viewLifecycleOwner) {
            it?.let {
                binding.downloadLink = it
                initPlayer(it.url)
            }
        }

        viewModel.progressVisible.observe(viewLifecycleOwner) {
            it?.let {
                binding.progressBarVisible = it
            }
        }

        viewModel.progressStatus.observe(viewLifecycleOwner) {
            it?.let {
                binding.progressBarStatus = it
            }
        }
    }

    fun copyUrl(url: String) {
        val clipData = ClipData.newPlainText("", url)
        clipboardManager.setPrimaryClip(clipData)

        Toast.makeText(requireContext(), getString(R.string.link_copied), Toast.LENGTH_LONG).show()
    }

    fun startDownload(url: String) {
        if (!ConnectionManager.checkInternetConnection(requireContext())) return

        val uri = Uri.parse(url)
        val request = DownloadManager.Request(uri)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        downloadManager.enqueue(request)
    }

    fun downloadViaBrowser(url: String) {
        if (!ConnectionManager.checkInternetConnection(requireContext())) return

        var newUrl = url
        if (!url.startsWith(getString(R.string.http_prefix)) && !url.startsWith(getString(R.string.https_prefix)))
            newUrl = getString(R.string.http_prefix) + url

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newUrl))
        startActivity(intent)
    }

    fun pasteUrl() {
        val clipData = clipboardManager.primaryClip
        if (clipData != null && clipData.itemCount > 0) {
            val url = clipData.getItemAt(0).text.toString()
            binding.txtVideoUrl.setText(url)
        }
    }

    fun downloadVideo(url: String) {
        if (!ConnectionManager.checkInternetConnection(requireContext()) || (viewModel.progressVisible.value != null && viewModel.progressVisible.value == true)) return
        viewModel.getDownloadTicket(url)
    }

    fun showPopup(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_popup, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.actionDownloadHistory -> findNavController().navigate(R.id.downloadToHistory)
                R.id.actionFaq -> showFaqDialog()
                R.id.actionAppInfo -> showAppInfoDialog()
                else -> return@setOnMenuItemClickListener false
            }
            true
        }
        popupMenu.show()
    }

    private fun showFaqDialog() {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_info, null)
        val dialogInfoTxt = view.findViewById<TextView>(R.id.dialogInfoTxt)

        val markwon = Markwon.builder(requireContext()).usePlugin(SoftBreakAddsNewLinePlugin.create()).build()
        markwon.setMarkdown(dialogInfoTxt, getString(R.string.faq_markdown))

        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setView(view)
        alertDialog.setNegativeButton(getString(R.string.btn_close), null)
        alertDialog.create().show()
    }

    private fun showAppInfoDialog() {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_info, null)
        val dialogInfoTxt = view.findViewById<TextView>(R.id.dialogInfoTxt)

        val markwon = Markwon.builder(requireContext()).usePlugin(SoftBreakAddsNewLinePlugin.create()).build()
        markwon.setMarkdown(dialogInfoTxt, getString(R.string.app_info_markdown))

        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setView(view)
        alertDialog.setNegativeButton(getString(R.string.btn_close), null)
        alertDialog.create().show()
    }

    private fun initPlayer(url: String) {
        binding.videoPlayer.player = player

        player.playWhenReady = true
        player.setMediaItem(MediaItem.fromUri(url))
        player.prepare()
        player.pause()
    }

    override fun onStop() {
        super.onStop()
        player.pause()
    }

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}