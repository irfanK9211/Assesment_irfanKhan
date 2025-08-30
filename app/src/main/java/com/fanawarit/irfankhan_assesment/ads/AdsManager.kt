package com.fanawarit.irfankhan_assesment.ads

import android.app.Activity
import android.content.Context
import com.fanawarit.irfankhan_assesment.BuildConfig
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AdsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var interstitial: InterstitialAd? = null
    var nativeAd: NativeAd? = null
        private set

    private var onNativeAdLoaded: (() -> Unit)? = null

    fun setOnNativeAdLoadedListener(listener: () -> Unit) {
        onNativeAdLoaded = listener
    }

    fun loadInterstitial() {

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            BuildConfig.INTERSTITIAL_AD_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitial = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitial = null
                }
            }
        )
    }

    fun showInterstitial(activity: Activity, onClosed: () -> Unit) {
        interstitial?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                loadInterstitial()
                onClosed()
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                onClosed()
            }
        }
        interstitial?.show(activity) ?: onClosed()
    }

    fun loadNativeAd(context: Context) {
        val adLoader = AdLoader.Builder(context, BuildConfig.NATIVE_AD_ID)
            .forNativeAd { ad: NativeAd ->
                nativeAd = ad
                onNativeAdLoaded?.invoke()

            }
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }
}
