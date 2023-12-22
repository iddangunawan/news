package com.example.news.domain.repository

import androidx.paging.PagingData
import com.example.news.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface TopHeadlinesRepository {
    fun getTopHeadlines(query: String = ""): Flow<PagingData<Article>>
}