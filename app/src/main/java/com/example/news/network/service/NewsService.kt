package com.example.news.network.service

import com.example.news.domain.model.TopHeadlinesResponse
import com.example.news.domain.model.TopHeadlinesSourcesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("apiKey") apiKey: String,
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("q") query: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
    ): TopHeadlinesResponse

    @GET("top-headlines/sources")
    suspend fun getTopHeadlinesSources(
        @Query("apiKey") apiKey: String,
        @Query("category") category: String,
        @Query("country") country: String,
    ): TopHeadlinesSourcesResponse
}