package com.serhatd.streamtapedownloader.ui.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.serhatd.streamtapedownloader.R
import com.serhatd.streamtapedownloader.databinding.ActivityMainBinding
import com.serhatd.streamtapedownloader.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        initObservers()

        binding.activity = this
        binding.viewModel = viewModel
    }

    private fun initObservers() {
        viewModel.downloadLink.observe(this) {
            it?.let {
                binding.downloadLink = it
            }
        }

        viewModel.toastObserver.observe(this) {
            it?.let {
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.progressVisible.observe(this) {
            it?.let {
                binding.progressBarVisible = it
            }
        }

        viewModel.progressMessage.observe(this) {
            it?.let {
                binding.lblProgress.text = it
            }
        }
    }

    fun copyDownloadUrl(url: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("", url)
        clipboardManager.setPrimaryClip(clipData)

        Toast.makeText(this, getString(R.string.download_link_copied), Toast.LENGTH_LONG).show()
    }

    fun downloadVideo(url: String) {
        val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(urlIntent)
    }
}