package com.map.dictionary

import android.app.Application
import com.map.dictionary.di.dictionaryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DictionaryApplication : Application() {

    companion object {
        lateinit var instance: DictionaryApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidContext(this@DictionaryApplication)
            modules(dictionaryModule)
        }
    }

}