package com.peterfarlow

import android.app.Application
import org.tinylog.kotlin.Logger

class SkeletonApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.debug { "application started" }
    }
}
