
package com.peterfarlow

import android.content.Context
import androidx.startup.Initializer
import androidx.work.WorkManagerInitializer
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

@Suppress("unused")
class KoinStartupInitializer : Initializer<KoinApplication> {
    override fun create(context: Context) =
        startKoin {
            androidContext(context)
            androidLogger()
            workManagerFactory()
            modules(appModule)
        }

    override fun dependencies() = listOf<Class<out Initializer<*>>>(WorkManagerInitializer::class.java)
}
