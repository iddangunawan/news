package com.example.news.network.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.news.domain.model.Article
import com.example.news.domain.model.Sources
import com.example.news.domain.repository.NewsRepository
import com.example.news.network.pagingsource.TopHeadlinesPagingSource
import com.example.news.network.pagingsource.TopHeadlinesSourcesPagingSource
import com.example.news.network.service.NewsService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val newsService: NewsService,
    private val apiKey: String,
    private val country: String,
    private val pageSize: Int,
) : NewsRepository {

    override fun getTopHeadlines(
        category: String,
        query: String,
    ): Flow<PagingData<Article>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = true
        ),
        pagingSourceFactory = {
            TopHeadlinesPagingSource(
                response = { pageNext ->
                    newsService.getTopHeadlines(
                        apiKey = apiKey,
                        country = country,
                        page = pageNext,
                        pageSize = pageSize,
                        category = category,
                        query = query,
                    )
                }
            )
        }
    ).flow

    override fun getTopHeadlinesSources(
        category: String,
        country: String,
    ): Flow<PagingData<Sources>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = true
        ),
        pagingSourceFactory = {
            TopHeadlinesSourcesPagingSource(
                response = { pageNext ->
                    newsService.getTopHeadlinesSources(
                        apiKey = apiKey,
                        category = category,
                        country = country,
                    )
                }
            )
        }
    ).flow
}