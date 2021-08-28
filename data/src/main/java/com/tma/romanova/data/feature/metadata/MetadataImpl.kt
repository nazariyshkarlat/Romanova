package com.tma.romanova.data.feature.metadata

import android.content.SharedPreferences
import com.tma.romanova.domain.feature.metadata.Metadata
import org.koin.androidx.compose.get
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject

object MetadataImpl : KoinComponent, Metadata {

    private val sharedPrefs by inject<SharedPreferences>()

    override val onBoardingWasCompleted: Boolean
    get() = sharedPrefs
        .getBoolean(ON_BOARDING_COMPLETED_KEY, false)

    override fun rememberOnBoardingCompletion(){
        sharedPrefs.edit().putBoolean(ON_BOARDING_COMPLETED_KEY, true).apply()
    }

    private const val ON_BOARDING_COMPLETED_KEY = "ON_BOARDING_COMPLETED_KEY"
}