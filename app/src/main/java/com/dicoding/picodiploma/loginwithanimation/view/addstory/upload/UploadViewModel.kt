package com.dicoding.picodiploma.loginwithanimation.view.addstory.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.AddStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.repository.StoryRepository
import java.io.File
import com.dicoding.picodiploma.loginwithanimation.data.domain.Result

class UploadViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun storyUpload(image: File, description: String): LiveData<Result<AddStoryResponse>> {
        return storyRepository.uploadStory(image, description)
    }
}