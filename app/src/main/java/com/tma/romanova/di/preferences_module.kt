package com.tma.romanova.di

import android.content.Context
import android.content.SharedPreferences
import com.tma.romanova.data.feature.metadata.MetadataImpl
import com.tma.romanova.domain.feature.metadata.Metadata
import org.koin.dsl.bind
import org.koin.dsl.module

private const val PREFERENCES_KEY = "ROMANOVA_PREFERENCES"

val preferencesModule = module {

    single {
        com.tma.romanova.core.application.getSharedPreferences(
            PREFERENCES_KEY, Context.MODE_PRIVATE
        )
    }

    single {
        MetadataImpl
    } bind Metadata::class
}