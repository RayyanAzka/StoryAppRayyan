package com.dicoding.picodiploma.loginwithanimation.view.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.local.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.AllStoriesResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.repository.AuthRepository
import com.dicoding.picodiploma.loginwithanimation.data.repository.StoryRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(storyRepository: StoryRepository, private val authRepository: AuthRepository) : ViewModel() {
    private val refresh = MutableLiveData<Unit>()

    val story: LiveData<PagingData<ListStoryItem>> by lazy { storyRepository.getAllStory().cachedIn(viewModelScope) }

    fun isLogin() = authRepository.isLogin()

    fun logout() = viewModelScope.launch {
        authRepository.logout()
    }

//    fun getAllStory() = refresh.switchMap {
//        storyRepository.getAllStory()
//    }

    fun refreshData() {
        refresh.value = Unit
    }

    init {
        refreshData()
    }

}