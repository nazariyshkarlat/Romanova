package com.tma.romanova

import android.app.Application
import com.tma.romanova.di.DI

class App : Application(){

    override fun onCreate() {
        super.onCreate()
        DI.init(this)
    }

}