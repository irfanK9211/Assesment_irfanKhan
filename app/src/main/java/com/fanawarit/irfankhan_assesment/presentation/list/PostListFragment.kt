package com.fanawarit.irfankhan_assesment.presentation.list

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fanawarit.irfankhan_assesment.ads.AdsManager
import com.fanawarit.irfankhan_assesment.databinding.FragmentPostListBinding
import com.google.android.gms.ads.AdRequest
import com.google.firebase.analytics.ktx.analytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig

@AndroidEntryPoint
class PostListFragment : Fragment() {

    private var _binding: FragmentPostListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PostListViewModel by viewModels()

    private lateinit var adapter: PostAdapter
    @Inject
    lateinit var adsManager: AdsManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        nativeAdsInPosts()

    }

    private fun initViews() {
        //Ab test variant
        val remoteConfig = Firebase.remoteConfig
                remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // CTA text
                        val ctaText = remoteConfig.getString("cta_text")
                       // myButton.text = ctaText

                        // Background color
                        val bgColor = remoteConfig.getString("post_item_background").ifEmpty { "#FFFFFF" }
                        try {
                            binding.root.setBackgroundColor(Color.parseColor(bgColor))
                        } catch (e: Exception) {
                            binding.root.setBackgroundColor(Color.parseColor("#FFFFFF"))
                        }

                        Log.d("ABTest", "CTA: $ctaText, Background: $bgColor")
                    }
                }

        adapter = PostAdapter { post ->
            viewModel.onPostClicked(post)
            val bundle = Bundle().apply {
                putString("post_id", post.id.toString())
                putString("title", post.title)
                putLong("timestamp", System.currentTimeMillis())
            }

            Firebase.analytics.logEvent("post_open", bundle)

            adsManager.showInterstitial(requireActivity()) {
                val action = PostListFragmentDirections
                    .actionPostListFragmentToPostDetailFragment(post.id)
                findNavController().navigate(action)
            }
        }
        binding.recyclerView.adapter = adapter

        viewModel.uiState.onEach { posts ->
            if (posts.isEmpty()) {
                binding.shimmerLayout.startShimmer()
                binding.shimmerLayout.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.shimmerLayout.stopShimmer()
                binding.shimmerLayout.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE

                val items = mutableListOf<ListItem>()
                posts.forEachIndexed { index, post ->
                    items.add(ListItem.PostItem(post))

                    if ((index + 1) % 3 == 0) {
                        adsManager.nativeAd?.let { nativeAd ->
                            items.add(ListItem.AdItem(nativeAd))
                        }
                    }
                }

                adapter.submitList(items)
            }
        }.launchIn(lifecycleScope)

        // Banner ad
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        // Load interstitial + native ad
        adsManager.loadInterstitial()
        adsManager.loadNativeAd(requireContext())
    }


    private fun nativeAdsInPosts(){
        adsManager.setOnNativeAdLoadedListener {
            val posts = viewModel.uiState.value
            val items = mutableListOf<ListItem>()
            posts.forEachIndexed { index, post ->
                items.add(ListItem.PostItem(post))
                if ((index + 1) % 3 == 0) {
                    adsManager.nativeAd?.let { nativeAd ->
                        items.add(ListItem.AdItem(nativeAd))
                    }
                }
            }
            adapter.submitList(items)
        }

    }

    override fun onResume() {
        super.onResume()
        val listBundle = Bundle().apply {
            putLong("timestamp", System.currentTimeMillis())
        }
        Firebase.analytics.logEvent("post_list_view", listBundle)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
