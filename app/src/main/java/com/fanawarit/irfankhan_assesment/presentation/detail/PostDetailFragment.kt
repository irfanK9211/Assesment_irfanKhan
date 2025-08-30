package com.fanawarit.irfankhan_assesment.presentation.detail

import android.R
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.fanawarit.irfankhan_assesment.databinding.FragmentPostDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostDetailFragment : Fragment() {

    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PostDetailViewModel by viewModels()
    private val args: PostDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()

    }

    private fun initViews() {

        lifecycleScope.launch {
            viewModel.loadPost(args.postId)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.post.collect { post ->
                post?.let {
                    val variant = viewModel.getDetailVariant()
                    if (variant == "new") {
                        binding.tvTitle.textSize = 24f
                        binding.tvTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.holo_blue_light))
                    } else {
                        binding.tvTitle.textSize = 18f
                        binding.tvTitle.setTextColor(Color.BLACK)
                    }
                    binding.tvTitle.text = it.title
                    binding.tvBody.text = it.body
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
