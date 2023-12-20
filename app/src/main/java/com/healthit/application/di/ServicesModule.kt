package com.healthit.application.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServicesModule {

    @Singleton
    @Provides
    fun provideHealthItService(@HealthItApi retrofit: Retrofit): com.healthit.application.data.network.api.HealthItApi {
        return retrofit.create(com.healthit.application.data.network.api.HealthItApi::class.java)
    }
}