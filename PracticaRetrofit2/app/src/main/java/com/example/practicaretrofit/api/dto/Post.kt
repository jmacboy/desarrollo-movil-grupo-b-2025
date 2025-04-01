package com.example.practicaretrofit.api.dto

import com.google.gson.annotations.SerializedName

typealias Posts = ArrayList<Post>

data class Post(
    @SerializedName("userId")
    val userID: Long,
    val id: Long,
    val title: String,
    val body: String
)
