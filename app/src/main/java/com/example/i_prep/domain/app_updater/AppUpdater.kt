package com.example.i_prep.domain.app_updater

import android.util.Log
import com.example.i_prep.common.gson
import com.example.i_prep.common.updateJSON
import com.example.i_prep.domain.app_updater.model.UpdateChangeLog
import com.google.gson.FieldNamingPolicy
import com.google.gson.JsonParser
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.gson.gson

class AppUpdater {

    private val latestVersion = "1.0.0"

    private val client = HttpClient(CIO) {
        engine {
            requestTimeout = 60_000 // 5 mins
        }

        install(ContentNegotiation) {
            gson() {
                setPrettyPrinting()
                setLenient()
                setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            }
        }
    }

    suspend fun checkUpdates() : UpdateChangeLog? {
        val response = client.get(updateJSON) {
            headers {
                append(HttpHeaders.ContentType, "application/json")
            }
        }

        if (!response.status.isSuccess()) {
            return null
        }

        Log.v("TAG", response.bodyAsText())
        return gson.fromJson(response.bodyAsText(), UpdateChangeLog::class.java)
    }
}