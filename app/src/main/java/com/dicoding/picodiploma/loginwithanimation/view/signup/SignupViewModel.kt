package com.dicoding.picodiploma.loginwithanimation.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.domain.Result
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.repository.AuthRepository

class SignupViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun signup(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> {
        return authRepository.signup(name, email, password)
    }
}