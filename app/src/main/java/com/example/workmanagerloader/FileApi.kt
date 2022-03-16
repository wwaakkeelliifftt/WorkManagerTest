package com.example.workmanagerloader

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET

interface FileApi {

    @GET("i/u/770x0/385966d3a01772fb6bd07521f7077bee.jpg")
    suspend fun downloadImage(): Response<ResponseBody>

    companion object {
        val instance by lazy {
            Retrofit.Builder()
                .baseUrl("https://lastfm.freetls.fastly.net/")
                .build()
                .create(FileApi::class.java)
        }
    }
}