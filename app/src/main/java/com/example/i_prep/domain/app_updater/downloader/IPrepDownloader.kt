package com.example.i_prep.domain.app_updater.downloader

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import com.example.i_prep.common.latestVersion
import java.io.File

class IPrepDownloader(private val context: Context) : Downloader {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    @SuppressLint("Range")
    override fun downloadFIle(url: String): Long {
        val timestamp = System.currentTimeMillis()

        val request = DownloadManager.Request("$url?t=$timestamp".toUri())
            .setMimeType("application/vnd.android.package-archive")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("I-Prep.apk")
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                url.split("/").last()
            )

        return downloadManager.enqueue(request)
    }
}

