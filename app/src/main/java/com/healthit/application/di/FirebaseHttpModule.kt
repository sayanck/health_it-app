package com.healthit.application.di

import com.healthit.application.BuildConfig
import com.healthit.application.data.network.CookiesInterceptor
import com.healthit.application.utils.constant.ApiConstant.OK_HTTP_CLIENT_TIME_OUT
import com.healthit.application.utils.retrofit.FlowCallAdapterFactory
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseHttpModule {

    @HealthItApi
    @Singleton
    @Provides
    fun providesBaseUrl(): String {
        return BuildConfig.FIREBASE_SERVER_IP
    }

    @HealthItApi
    @Singleton
    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @HealthItApi
    @Provides
    fun providesCookiesInterceptor(): CookiesInterceptor {
        return CookiesInterceptor()
    }

    @HealthItApi
    @Singleton
    @Provides
    fun provideOkHttpClient(
        @HealthItApi loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()
        okHttpClient.callTimeout(OK_HTTP_CLIENT_TIME_OUT, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(OK_HTTP_CLIENT_TIME_OUT, TimeUnit.SECONDS)
        okHttpClient.readTimeout(OK_HTTP_CLIENT_TIME_OUT, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(OK_HTTP_CLIENT_TIME_OUT, TimeUnit.SECONDS)
        okHttpClient.addInterceptor(loggingInterceptor)
        okHttpClient.build()
        return okHttpClient.build()
    }

    @HealthItApi
    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @HealthItApi
    @Singleton
    @Provides
    fun provideRetrofitClient(
        @HealthItApi okHttpClient: OkHttpClient,
        @HealthItApi baseUrl: String,
        @HealthItApi converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addCallAdapterFactory(FlowCallAdapterFactory.create())
            .build()
    }
}
