package com.dicoding.picodiploma.loginwithanimation.view.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.domain.Result
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.local.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> {
        return authRepository.login(email, password)
    }
}