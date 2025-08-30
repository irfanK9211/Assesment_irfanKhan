package com.fanawarit.irfankhan_assesment.presentation.list

import com.fanawarit.irfankhan_assesment.domain.model.Post
import com.google.android.gms.ads.nativead.NativeAd

sealed class ListItem {
    data class PostItem(val post: Post) : ListItem()
    data class AdItem(val ad: NativeAd) : ListItem()
}
