package com.example.news.domain.repository

import androidx.paging.PagingData
import com.example.news.domain.model.Article
import com.example.news.domain.model.Sources
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getTopHeadlines(category: String, query: String = ""): Flow<PagingData<Article>>
    fun getTopHeadlinesSources(
        category: String = "",
        country: String = "",
    ): Flow<PagingData<Sources>>
}