package com.tma.romanova.di

import android.app.Application
import com.tma.romanova.core.putApplication
import com.tma.romanova.core.putMetadata
import com.tma.romanova.di.feature.mainScreenModule
import com.tma.romanova.di.feature.nowPlayingModule
import com.tma.romanova.di.feature.onBoardingModule
import com.tma.romanova.di.feature.playerModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

object DI{

    fun init(application: Application){
        putApplication(application)
        startKoin{
            androidLogger()
            androidContext(application)
            modules(
                networkModule,
                preferencesModule,
                onBoardingModule,
                mainScreenModule,
                playerModule,
                nowPlayingModule
            )
        }
        putMetadata(GlobalContext.get().get())
    }

}