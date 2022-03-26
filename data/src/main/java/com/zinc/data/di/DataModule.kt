package com.zinc.data.di

import com.zinc.data.api.MyBuryApi
import com.zinc.data.repository.MyBuryRepository
import com.zinc.data.repository.MyBuryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(includes = [DataModule.ApiModule::class])
abstract class DataModule {
    @Binds
    abstract fun bindsMyBuryRepository(
        repository: MyBuryRepositoryImpl
    ): MyBuryRepository

    @InstallIn(SingletonComponent::class)
    @Module
    internal object ApiModule {

        @Provides
        @Singleton
        fun provideMyBuryApi(
            okHttpClient: OkHttpClient
        ): MyBuryApi {
            return Retrofit.Builder()
                .baseUrl("https://www.my-bury.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(MyBuryApi::class.java)
        }
    }
}