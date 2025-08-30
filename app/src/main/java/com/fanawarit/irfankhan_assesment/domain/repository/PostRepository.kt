package com.fanawarit.irfankhan_assesment.domain.repository

import com.fanawarit.irfankhan_assesment.domain.model.Post

interface PostRepository {
    suspend fun getPosts(): List<Post>
    suspend fun getPost(id: Int): Post
}