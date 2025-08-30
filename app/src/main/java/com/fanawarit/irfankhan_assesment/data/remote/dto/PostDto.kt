package com.fanawarit.irfankhan_assesment.data.remote.dto

import com.fanawarit.irfankhan_assesment.domain.model.Post

data class PostDto(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)
    fun PostDto.toDomain() :Post {
        return Post(id = id, title = title, body = body, userId = userId)
    }


