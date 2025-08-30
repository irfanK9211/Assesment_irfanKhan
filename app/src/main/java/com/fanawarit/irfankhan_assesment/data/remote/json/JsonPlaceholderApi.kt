package com.fanawarit.irfankhan_assesment.data.remote.json

import com.fanawarit.irfankhan_assesment.data.remote.dto.PostDto
import retrofit2.http.GET
import retrofit2.http.Path

interface JsonPlaceholderApi {
    @GET("posts")
    suspend fun getPosts(): List<PostDto>

    @GET("posts/{id}")
    suspend fun getPost(@Path("id") id: Int): PostDto
}