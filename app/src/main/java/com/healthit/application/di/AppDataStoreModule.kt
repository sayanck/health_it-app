package com.healthit.application.di

import android.content.Context
import com.healthit.application.data.datastore.AppDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppDataStoreModule {

    @Singleton
    @Provides
    fun provideAppDataStore(
        @ApplicationContext app: Context
    ): AppDataStore = AppDataStore(app)
}