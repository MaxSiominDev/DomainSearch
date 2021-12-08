package dev.maxsiomin.domainsearch

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    init {
        /** If release build mode enabled [Timber] will log nothing */
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}
