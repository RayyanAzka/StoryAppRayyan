package com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit

import com.dicoding.picodiploma.loginwithanimation.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiConfig {
    val client = OkHttpClient.Builder()
        .addNetworkInterceptor(
            HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.BODY
            )).build()
    fun getApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create()
    }
}