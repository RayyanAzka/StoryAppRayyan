package com.dicoding.picodiploma.loginwithanimation.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.dicoding.picodiploma.loginwithanimation.data.local.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.remote.RemoteDataSource
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.data.domain.Result

class AuthRepository(private val remoteDataSource: RemoteDataSource, private val userPreference: UserPreference) {
    fun signup(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = remoteDataSource.signup(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = remoteDataSource.login(email, password)
            userPreference.saveToken(response.loginResult?.token.toString())
            userPreference.saveLogin(true)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun isLogin() = userPreference.isLogin().asLiveData()

    suspend fun logout() {
        Log.d("message", "Logout berhasil")
        userPreference.run {
            saveLogin(false)
            deleteToken()
        }
    }
}