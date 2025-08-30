package com.fanawarit.irfankhan_assesment.presentation.list

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fanawarit.irfankhan_assesment.databinding.ItemNativeAdBinding
import com.fanawarit.irfankhan_assesment.databinding.ItemPostBinding
import com.fanawarit.irfankhan_assesment.domain.model.Post
import com.google.android.gms.ads.nativead.NativeAd


class PostAdapter(
    private val onClick: (Post) -> Unit
) : ListAdapter<ListItem, RecyclerView.ViewHolder>(Diff) {

    companion object {
        private const val VIEW_TYPE_POST = 0
        private const val VIEW_TYPE_AD = 1
    }

    object Diff : DiffUtil.ItemCallback<ListItem>() {
        override fun areItemsTheSame(old: ListItem, new: ListItem): Boolean {
            return when {
                old is ListItem.PostItem && new is ListItem.PostItem -> old.post.id == new.post.id
                old is ListItem.AdItem && new is ListItem.AdItem -> old.ad.hashCode() == new.ad.hashCode()
                else -> false
            }
        }

        override fun areContentsTheSame(old: ListItem, new: ListItem): Boolean = old == new
    }

    inner class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Post) {
            binding.tvTitle.text = item.title
            binding.tvBody.text = item.body.take(100) + "..."
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    inner class AdViewHolder(private val binding: ItemNativeAdBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(nativeAd: NativeAd) {
            val adView = binding.root as com.google.android.gms.ads.nativead.NativeAdView

            adView.headlineView = binding.adHeadline
            adView.bodyView = binding.adBody
            adView.callToActionView = binding.adCallToAction
            adView.iconView = binding.adAppIcon
            adView.mediaView = binding.adMedia
            adView.advertiserView = binding.adAdvertiser

            (adView.headlineView as TextView).text = nativeAd.headline
            (adView.bodyView as TextView).text = nativeAd.body
            (adView.callToActionView as Button).text = nativeAd.callToAction

            (adView.advertiserView as TextView).text = nativeAd.advertiser

            nativeAd.icon?.let {
                (adView.iconView as ImageView).setImageDrawable(it.drawable)
                adView.iconView?.visibility = View.VISIBLE
            } ?: run { adView.iconView?.visibility = View.GONE }

            adView.setNativeAd(nativeAd)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ListItem.PostItem -> VIEW_TYPE_POST
            is ListItem.AdItem -> VIEW_TYPE_AD
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_POST) {
            val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            PostViewHolder(binding)
        } else {
            val binding = ItemNativeAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AdViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ListItem.PostItem -> (holder as PostViewHolder).bind(item.post)
            is ListItem.AdItem -> (holder as AdViewHolder).bind(item.ad)
        }
    }
}
