package com.pixel.toctalk.app

import android.app.Application
import com.pixel.toctalk.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TokTalkApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TokTalkApp)
            androidLogger()
            modules(appModule)
        }
    }
}
