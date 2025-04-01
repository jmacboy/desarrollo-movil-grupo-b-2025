package com.example.practicaretrofit.repositories

import com.example.practicaretrofit.api.JSONPlaceHolderService
import com.example.practicaretrofit.api.dto.Posts

object PostRepository {
    suspend fun getPostsList(): Posts {
        val api = RetrofitRepository
            .getRetrofitInstance()
            .create(JSONPlaceHolderService::class.java)
        return api.getPosts()
    }
}