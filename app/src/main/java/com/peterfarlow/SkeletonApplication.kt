package com.peterfarlow

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.tinylog.kotlin.Logger
import kotlin.time.Duration.Companion.seconds

class SkeletonApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.debug { "application created" }
        Logger.debug { packageName }
        val appLifecycle = ProcessLifecycleOwner.get()
        appLifecycle.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                Logger.debug { "application started" }
            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                Logger.debug { "application stopped" }
            }
        })
        appLifecycle.lifecycleScope.launch {
            while (true) {
                Logger.debug { "I'm alive" }
                delay(5.seconds)
            }
        }
    }
}
