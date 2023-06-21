package com.dicoding.picodiploma.loginwithanimation.di

import android.content.Context
import com.dicoding.picodiploma.loginwithanimation.data.local.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.remote.RemoteDataSource
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.repository.AuthRepository
import com.dicoding.picodiploma.loginwithanimation.data.repository.StoryRepository

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val apiService = ApiConfig.getApiService()
        val remoteDataSource = RemoteDataSource(apiService)
        val userPreference = UserPreference.getInstance(context)
        return AuthRepository(remoteDataSource, userPreference)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val remoteDataSource = RemoteDataSource(apiService)
        val userPreference = UserPreference.getInstance(context)
        return StoryRepository(remoteDataSource, userPreference, apiService)
    }
}