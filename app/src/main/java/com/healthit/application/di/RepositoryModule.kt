package com.healthit.application.di

import com.healthit.application.repository.DataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideDataRepository(
        healthItApi: com.healthit.application.data.network.api.HealthItApi,
    ): DataRepository {
        return DataRepository(
            healthItApi
        )
    }
}