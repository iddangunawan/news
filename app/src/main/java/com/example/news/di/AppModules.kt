package com.example.news.di

import com.example.news.BuildConfig
import com.example.news.domain.repository.NewsRepository
import com.example.news.network.repository.NewsRepositoryImpl
import com.example.news.network.service.NewsService
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
    @Named(Const.Provides.BASE_URL_NEWS)
    fun provideBaseUrlNews(): String = Const.BASE_URL_NEWS

    @Provides
    @Named(Const.Provides.API_KEY_NEWS)
    fun provideAPIKeyNews(): String = BuildConfig.API_KEY_NEWS

    @Provides
    @Named(Const.Provides.COUNTRY_NEWS)
    fun provideCountryNews(): String = Const.COUNTRY_ID

    @Provides
    @Named(Const.Provides.PAGE_SIZE)
    fun providePageSize(): Int = Const.PAGE_SIZE

    @Provides
    fun provideRetrofitNews(
        @Named(Const.Provides.BASE_URL_NEWS) baseUrl: String,
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
    fun provideNewsService(
        retrofit: Retrofit
    ): NewsService = retrofit.create(NewsService::class.java)

    @Provides
    fun provideNewsRepository(
        newsService: NewsService,
        @Named(Const.Provides.API_KEY_NEWS) apiKey: String,
        @Named(Const.Provides.COUNTRY_NEWS) country: String,
        @Named(Const.Provides.PAGE_SIZE) pageSize: Int,
    ): NewsRepository = NewsRepositoryImpl(
        newsService = newsService,
        apiKey = apiKey,
        country = country,
        pageSize = pageSize,
    )
}