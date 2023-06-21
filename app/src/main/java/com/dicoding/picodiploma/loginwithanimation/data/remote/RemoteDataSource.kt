package com.dicoding.picodiploma.loginwithanimation.data.remote

import android.util.Log
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.AddStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.AllStoriesResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun signup(name: String, email: String, password: String): RegisterResponse {
        Log.d("name", name)
        Log.d("email", email)
        Log.d("password", password)
        return apiService.signup(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        Log.d("email", email)
        return apiService.login(email, password)
    }

    suspend fun getAllStory(token: String, page: Int? = null, size: Int? = null, location: Int = 0): AllStoriesResponse {
        return apiService.getAllStory(token, page, size, location)
    }

    suspend fun detailStory(token: String, id: String) = apiService.detailStory(token, id)

    suspend fun uploadStory(token: String, image: File, description: String): AddStoryResponse {
        val requestDescription = description.toRequestBody("text/plain".toMediaType())
        val requestImage = image.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart = MultipartBody.Part.createFormData(
            "photo",
            image.name,
            requestImage
        )

        return apiService.uploadStory(token, imageMultipart, requestDescription)
    }
}