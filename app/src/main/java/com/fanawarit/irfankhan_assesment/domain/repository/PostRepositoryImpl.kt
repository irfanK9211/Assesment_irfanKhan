package com.fanawarit.irfankhan_assesment.domain.repository

import com.fanawarit.irfankhan_assesment.data.remote.dto.toDomain
import com.fanawarit.irfankhan_assesment.data.remote.json.JsonPlaceholderApi
import com.fanawarit.irfankhan_assesment.domain.model.Post
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val api: JsonPlaceholderApi
): PostRepository {
    override suspend fun getPosts(): List<Post> =
        api.getPosts().map { it.toDomain() }

    override suspend fun getPost(id: Int): Post =
        api.getPost(id).toDomain()
}