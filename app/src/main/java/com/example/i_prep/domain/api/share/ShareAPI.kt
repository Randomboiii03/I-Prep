package com.example.i_prep.domain.api.share

import com.example.i_prep.BuildConfig
import com.example.i_prep.common.displayLog
import com.example.i_prep.common.gson
import com.example.i_prep.domain.api.share.model.Create
import com.example.i_prep.domain.api.share.model.GetPaste
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.random.Random

class ShareAPI {
    private val endPoint = "https://api.pastelink.net"
    private val cookie = "PHPSESSID=bc9hie0kdveu1tign27vcums9m"

    private fun generateRandomPassword(length: Int): String {
        val lowercaseLetters = ('a'..'z').toList()
        val uppercaseLetters = ('A'..'Z').toList()
        val digits = ('0'..'9').toList()
        val specialChars = listOf<Char>('!', '@', '#', '$', '%', '&', '*', '(', ')', '-', '_', '+', '=', '{', '}', '[', ']', '|', '\\', ',', '.', '/', '?')
        val charPool = lowercaseLetters + uppercaseLetters + digits + specialChars

        return (1..length).map { charPool.random(Random) }.joinToString("")
    }


    suspend fun share(encodedTest: String): String {
        val client = OkHttpClient()
//        val mediaType = "text/plain".toMediaType()
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("api_key",BuildConfig.pasteLinkKey)
            .addFormDataPart("body",encodedTest)
            .addFormDataPart("option_visibility","private")
            .addFormDataPart("access_password",generateRandomPassword(6))
            .build()
        val request = Request.Builder()
            .url("$endPoint/create-paste")
            .post(body)
            .addHeader("Cookie", cookie)
            .build()
        val response = client.newCall(request).execute()

        val json = gson.fromJson(response.body.string(), Create::class.java)
        displayLog("ShareAPI", json.toString())
        return json.url
    }

    suspend fun getShared(url: String): String {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("$endPoint/get-paste?api_key=${BuildConfig.pasteLinkKey}&url=$url")
            .addHeader("Cookie", cookie)
            .build()
        val response = client.newCall(request).execute()

        val json = gson.fromJson(response.body.string(), GetPaste::class.java)
        return json.body
    }
}