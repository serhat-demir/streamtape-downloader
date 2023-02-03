package com.serhatd.streamtapedownloader.ui.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.serhatd.streamtapedownloader.R
import com.serhatd.streamtapedownloader.data.prefs.SharedPrefs
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val view: View = layoutInflater.inflate(R.layout.dialog_credentials, null)
        val txtLogin: EditText = view.findViewById(R.id.dialogTxtLogin)
        val txtPassword: EditText = view.findViewById(R.id.dialogTxtPassword)

        txtLogin.setText(SharedPrefs.getSharedPreference(this@MainActivity, getString(R.string.prefs_login)))
        txtPassword.setText(SharedPrefs.getSharedPreference(this@MainActivity, getString(R.string.prefs_key)))

        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle(getString(R.string.app_name))
        builder.setView(view)
        builder.setNegativeButton(getString(R.string.cancel), null)
        builder.setPositiveButton(getString(R.string.save)) { _, _ ->
            val login: String = txtLogin.text.toString().trim()
            val password: String = txtPassword.text.toString().trim()

            viewModel.setCredentials(login, password)
        }
        builder.create().show();

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
}