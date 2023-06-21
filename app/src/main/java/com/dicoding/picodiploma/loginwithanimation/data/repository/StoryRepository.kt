package com.dicoding.picodiploma.loginwithanimation.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.picodiploma.loginwithanimation.data.local.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.remote.RemoteDataSource
import java.io.File
import com.dicoding.picodiploma.loginwithanimation.data.domain.Result
import com.dicoding.picodiploma.loginwithanimation.data.paging.StoryPagingSource
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.AddStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.Story
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first

class StoryRepository(private val remoteDataSource: RemoteDataSource, private val userPreference: UserPreference, private val apiService: ApiService) {
    fun getAllStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, userPreference)
            }
        ).liveData
    }

    fun getDetailStory(id: String): LiveData<Result<Story>> = liveData {
        emit(Result.Loading)
        try {
            val token = userPreference.getToken().first()
            val response = remoteDataSource.detailStory("Bearer $token", id)
            val result = response.story
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun uploadStory(image: File, description: String): LiveData<Result<AddStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = userPreference.getToken().first()
            val response = remoteDataSource.uploadStory("Bearer $token", image, description)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStoryWithLocation(): LiveData<Result<List<ListStoryItem>>> = liveData {
        emit(Result.Loading)
        try {
            val token = userPreference.getToken().first()
            val response = remoteDataSource.getAllStory("Bearer $token", location = 1)
            val result = response.listStory
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}