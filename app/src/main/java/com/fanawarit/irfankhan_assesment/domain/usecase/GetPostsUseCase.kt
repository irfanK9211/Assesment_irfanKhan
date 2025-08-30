package com.fanawarit.irfankhan_assesment.domain.usecase

import com.fanawarit.irfankhan_assesment.domain.repository.PostRepository
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(private val repo: PostRepository) {
    suspend operator fun invoke() = repo.getPosts()
}
