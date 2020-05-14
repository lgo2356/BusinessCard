package kr.co.businesscard

import retrofit2.Call
import retrofit2.http.GET

interface RetrofitAPI {
    @GET("/")
    fun getUrlList(): Call<String>
}