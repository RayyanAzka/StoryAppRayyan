package com.dicoding.picodiploma.loginwithanimation.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.picodiploma.loginwithanimation.data.local.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.local.entity.StoryEntity
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first
import java.lang.Exception

class StoryPagingSource(private val apiService: ApiService, private val userPreference: UserPreference): PagingSource<Int, ListStoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try  {
            val token = "Bearer ${userPreference.getToken().first()}"
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getAllStory(token, position, params.loadSize)

            Log.d("List Story", response.toString())
            LoadResult.Page(
                data = response.listStory.map {
                    ListStoryItem(
                        id = it.id,
                        name = it.name,
                        photoUrl = it.photoUrl,
                        createdAt = it.createdAt,
                        description = it.description,
                        lat = it.lat,
                        lon = it.lon
                    )
                },
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position -1,
                nextKey = if (response.listStory.isEmpty()) null else position +1
            )
        } catch (exc: Exception) {
            return LoadResult.Error(exc)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}