package com.fanawarit.irfankhan_assesment.analytics

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(app: Application): FirebaseAnalytics =
        FirebaseAnalytics.getInstance(app)

    @Provides
    @Singleton
    fun provideRemoteConfig(): FirebaseRemoteConfig {
        val rc = FirebaseRemoteConfig.getInstance()
        val defaults = mapOf("detail_variant" to "control")
        rc.setDefaultsAsync(defaults)
        rc.fetchAndActivate()
        return rc
    }
}
