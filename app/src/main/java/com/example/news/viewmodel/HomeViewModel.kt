package com.example.news.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.news.domain.model.Article
import com.example.news.domain.repository.TopHeadlinesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    topHeadlinesRepository: TopHeadlinesRepository,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val articleListState: Flow<PagingData<Article>> =
        topHeadlinesRepository.getTopHeadlines(query = searchQuery.value).cachedIn(viewModelScope)
}

data class ArticleListUiState(
    val articleList: Flow<PagingData<Article>>? = emptyFlow(),
) {
    companion object {
        val default: ArticleListUiState = ArticleListUiState(articleList = emptyFlow())
    }
}

data class FilterArticleListUiState(
    val query: String = "",
) {
    companion object {
        val default: FilterArticleListUiState = FilterArticleListUiState(query = "")
    }
}