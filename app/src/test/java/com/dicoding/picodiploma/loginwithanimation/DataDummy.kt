package com.dicoding.picodiploma.loginwithanimation

import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                photoUrl = i.toString(),
                createdAt = i.toString(),
                description = i.toString(),
                id = i.toString(),
                lat = i.toDouble(),
                lon = i.toDouble(),
                name = i.toString()
            )
            items.add(story)
        }
        return items
    }
}