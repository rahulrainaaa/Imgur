package com.application.imgur

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class HttpHandler {

    private val TAG = "HttpHandler"

    /**
     * Suspended method to call GET service on IO Dispatcher thread and return the model.
     */
    suspend fun get(url: String): WebCallModel {

        return withContext(Dispatchers.IO) {

            Log.d(TAG, url)
            val client: OkHttpClient = OkHttpClient()
            val request = Request.Builder()
                .addHeader("Authorization", "Client-ID 137cda6b5008a7c")
                .url(url)
                .build()

            val model = WebCallModel()
            try {
                val response = client.newCall(request).execute()
                val body = response.body!!.string()
                model.code = response.code
                model.message = response.message
                model.body = body
            } catch (e: Exception) {
                model.code = -1     // 0 = case of exception.
                model.message = e.message!!
                model.body = ""
            }
            model
        }
    }

    /**
     * Response model returned from suspended get() method call.
     */
    class WebCallModel {

        var code = -1
        var message = ""
        var body = ""
    }
}
