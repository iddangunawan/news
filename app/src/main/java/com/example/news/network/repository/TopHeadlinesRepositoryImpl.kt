package com.example.news.network.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.news.domain.model.Article
import com.example.news.domain.repository.TopHeadlinesRepository
import com.example.news.network.pagingsource.TopHeadlinesPagingSource
import com.example.news.network.service.TopHeadlinesService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopHeadlinesRepositoryImpl @Inject constructor(
    private val topHeadlinesService: TopHeadlinesService,
    private val apiKey: String,
    private val country: String,
    private val pageSize: Int,
) : TopHeadlinesRepository {

    override fun getTopHeadlines(query: String): Flow<PagingData<Article>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = true
        ),
        pagingSourceFactory = {
            TopHeadlinesPagingSource(
                response = { pageNext ->
                    topHeadlinesService.getTopHeadlines(
                        apiKey = apiKey,
                        country = country,
                        page = pageNext,
                        pageSize = pageSize,
                        q = query,
                    )
                }
            )
        }
    ).flow
}