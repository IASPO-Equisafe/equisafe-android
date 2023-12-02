package com.iaspo.equisafe

import android.app.Application
import com.iaspo.equisafe.core.di.dataStoreModule
import com.iaspo.equisafe.core.di.networkModule
import com.iaspo.equisafe.core.di.repositoryModule
import com.iaspo.equisafe.di.userCaseModule
import com.iaspo.equisafe.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    networkModule,
                    dataStoreModule,
                    userCaseModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
    }
}