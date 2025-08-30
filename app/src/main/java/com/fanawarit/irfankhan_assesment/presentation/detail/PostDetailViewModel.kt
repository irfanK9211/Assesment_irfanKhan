package com.fanawarit.irfankhan_assesment.presentation.detail

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.fanawarit.irfankhan_assesment.data.remote.remoteconfig.RemoteConfigManager
import com.fanawarit.irfankhan_assesment.domain.model.Post
import com.fanawarit.irfankhan_assesment.domain.repository.PostRepository
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val repo: PostRepository,
    private val analytics: FirebaseAnalytics,
    private val rc: RemoteConfigManager
): ViewModel() {
    private val _post = MutableStateFlow<Post?>(null)
    val post: StateFlow<Post?> = _post.asStateFlow()

    suspend fun loadPost(id: Int) {
        val p = repo.getPost(id)
        _post.value = p

        val bundle = Bundle().apply {
            putString("post_id", id.toString())
        }
        analytics.logEvent("post_detail_view", bundle)
    }

    fun getDetailVariant(): String = rc.getString("detail_variant") ?: "control"
}