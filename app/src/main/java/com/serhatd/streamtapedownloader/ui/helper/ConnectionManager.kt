package com.serhatd.streamtapedownloader.ui.helper

import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AlertDialog
import com.serhatd.streamtapedownloader.R
import kotlin.system.exitProcess

object ConnectionManager {
    private fun hasActiveInternetConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    fun checkInternetConnection(context: Context): Boolean {
        if (hasActiveInternetConnection(context)) return true

        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.alert_dialog_title_no_internet)
        builder.setMessage(R.string.alert_dialog_message_no_internet)
        builder.setPositiveButton(R.string.alert_dialog_button_ok) { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
            exitProcess(0)
        }
        builder.setNegativeButton(R.string.alert_dialog_button_try_again) { dialog: DialogInterface, _: Int ->
            checkInternetConnection(context)
        }
        builder.setCancelable(false)
        builder.show()

        return false
    }
}