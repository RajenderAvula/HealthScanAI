package com.example.healthscanai.data.remote

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Url
import retrofit2.Response

interface SyncApi {
    @GET
    suspend fun ping(@Url url: String): Response<ResponseBody>

    @POST
    suspend fun sync(@Url url: String, @Body body: SyncPayload): Response<ResponseBody>

    companion object {
        fun create(): SyncApi {
            return Retrofit.Builder()
                .baseUrl("https://example.invalid/")
                .client(OkHttpClient.Builder().build())
                .build()
                .create(SyncApi::class.java)
        }
    }
}
