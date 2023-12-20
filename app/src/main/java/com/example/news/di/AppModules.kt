package com.example.news.di

import com.example.news.network.service.TopHeadlinesService
import com.example.news.utils.Const
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class AppModules {
    @Provides
    @Named("BASE_URL")
    fun provideBaseUrl(): String = Const.BASE_URL

    @Provides
    @Named("API_KEY")
    fun provideAPIAKey(): String = Const.API_KEY

    @Provides
    @Named("PAGE_SIZE")
    fun providePageSize(): Int = Const.PAGE_SIZE

    @Provides
    fun provideRetrofit(
        @Named("BASE_URL") baseUrl: String,
    ): Retrofit {
        val client = OkHttpClient
            .Builder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun provideTopHeadlinesService(
        retrofit: Retrofit
    ): TopHeadlinesService = retrofit.create(TopHeadlinesService::class.java)
}