package com.example.news.network.service

import com.example.news.domain.model.TopHeadlinesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TopHeadlinesService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("apiKey") apiKey: String,
        @Query("country") country: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
        @Query("q") query: String,
    ): TopHeadlinesResponse
}