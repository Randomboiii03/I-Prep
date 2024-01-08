package com.example.i_prep.domain.app_updater.downloader

interface Downloader {
    fun downloadFIle(url: String): Long
}