package kr.co.businesscard

import android.accounts.NetworkErrorException
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

object Api {
    private val client = OkHttpClient()


    fun requestApi(): String? {
        return getJsonString(client, "https://api.github.com/")
    }

    private fun getJsonString(client: OkHttpClient, url: String): String? {
        return try {
            val request: Request = Request.Builder().url(url).build()
            val response: Response = client.newCall(request).execute()

            response.body?.string()
        } catch (e: NetworkErrorException) {
            e.printStackTrace()

            null
        }
    }

}
