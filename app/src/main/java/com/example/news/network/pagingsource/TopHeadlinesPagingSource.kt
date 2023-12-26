package com.example.news.network.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.news.domain.model.Article
import com.example.news.domain.model.TopHeadlinesResponse
import com.example.news.utils.parseErrorMessageFromJson
import retrofit2.HttpException

class TopHeadlinesPagingSource(
    private val response: suspend (Int) -> TopHeadlinesResponse,
) : PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val nextPage = params.key ?: 1
            val articleList = response.invoke(nextPage)
            LoadResult.Page(
                data = articleList.articles,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (articleList.articles.isEmpty()) null else nextPage + 1
            )
        } catch (exception: HttpException) {
            val errorBody = exception.response()?.errorBody()?.string()
            val errorMessage = parseErrorMessageFromJson(errorBody)
            LoadResult.Error(Exception("Error HTTP ${exception.code()}:\n$errorMessage"))
        } catch (exception: Exception) {
            println("Exception ${exception.message}")
            LoadResult.Error(exception)
        }
    }
}