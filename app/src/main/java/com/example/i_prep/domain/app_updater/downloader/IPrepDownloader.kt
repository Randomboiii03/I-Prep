package com.example.i_prep.domain.app_updater.downloader

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri

class IPrepDownloader(private val context: Context): Downloader {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override fun downloadFIle(url: String, desc: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("application/vnd.android.package-archive")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("I-Prep.apk")
            .setDescription("Release Notes:\n\n• $desc")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "I-Prep.apk")

        return downloadManager.enqueue(request)
    }
}