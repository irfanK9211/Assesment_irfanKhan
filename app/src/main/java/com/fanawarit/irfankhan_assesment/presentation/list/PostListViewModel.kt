package com.fanawarit.irfankhan_assesment.presentation.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fanawarit.irfankhan_assesment.domain.model.Post
import com.fanawarit.irfankhan_assesment.domain.usecase.GetPostsUseCase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val firebaseAnalytics: FirebaseAnalytics
): ViewModel() {

    private val _uiState = MutableStateFlow<List<Post>>(emptyList())
    val uiState: StateFlow<List<Post>> = _uiState.asStateFlow()

    init { loadPosts() }

    private fun loadPosts() {
        viewModelScope.launch {
            try {
                val posts = getPostsUseCase()
                _uiState.value = posts

                firebaseAnalytics.logEvent("post_list_impression") {
                    param("count", posts.size.toLong())
                }
            } catch (e: Exception) {
                Log.d("dsfsdfs", "loadPosts: "+e.cause)
                Log.d("dsfsdfs", "loadPosts: "+e.message)

                // handle error
            }
        }
    }

    fun onPostClicked(post: Post) {
        firebaseAnalytics.logEvent("post_open") {
            param("post_id", post.id.toLong())
        }
    }
}