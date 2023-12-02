package com.iaspo.equisafe.core.di

import com.iaspo.equisafe.BuildConfig
import com.iaspo.equisafe.core.data.local.UserPreferences
import com.iaspo.equisafe.core.data.repository.AuthRepository
import com.iaspo.equisafe.core.data.network.retrofit.ApiService
import com.iaspo.equisafe.core.data.repository.AccountRepository
import com.iaspo.equisafe.core.data.repository.EmergencyRepository
import com.iaspo.equisafe.core.data.repository.HomeRepository
import com.iaspo.equisafe.core.data.repository.LearningRepository
import com.iaspo.equisafe.core.domain.repository.IAccountRepository
import com.iaspo.equisafe.core.domain.repository.IAuthRepository
import com.iaspo.equisafe.core.domain.repository.IEmergencyRepository
import com.iaspo.equisafe.core.domain.repository.IHomeRepository
import com.iaspo.equisafe.core.domain.repository.ILearningRepository
import com.iaspo.equisafe.core.utils.AppExecutors
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://equisafe.et.r.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val dataStoreModule = module {
    single {
        UserPreferences(androidContext())
    }
}

val repositoryModule = module {
    factory { AppExecutors() }
    single<IAuthRepository> { AuthRepository(get(), get()) }
    single<IHomeRepository> { HomeRepository(get(), get()) }
    single<IAccountRepository> { AccountRepository(get(), get()) }
    single<IEmergencyRepository> { EmergencyRepository(get(), get()) }
    single<ILearningRepository> { LearningRepository(get(), get()) }
}