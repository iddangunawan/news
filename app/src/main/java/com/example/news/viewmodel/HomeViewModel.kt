package com.example.news.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.news.domain.model.Article
import com.example.news.domain.repository.TopHeadlinesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    topHeadlinesRepository: TopHeadlinesRepository,
) : ViewModel() {

    private val _filterArticleListState = MutableStateFlow(FilterArticleListUiState())
    private val filterArticleListState = _filterArticleListState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val articleListState: StateFlow<ArticleListUiState> = filterArticleListState.flatMapLatest {
        flow {
            val articleList = topHeadlinesRepository.getTopHeadlines(
                category = it.category,
                query = it.query,
            ).cachedIn(viewModelScope)
            emit(ArticleListUiState(articleList = articleList))
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        ArticleListUiState.default
    )

    fun onEvent(homeViewUiEvent: HomeViewUiEvent) {
        when (homeViewUiEvent) {
            is HomeViewUiEvent.CategoryArticle -> {
                _filterArticleListState.value = _filterArticleListState.value.copy(
                    category = homeViewUiEvent.category
                )
            }

            is HomeViewUiEvent.SearchArticle -> {
                _filterArticleListState.value = _filterArticleListState.value.copy(
                    query = homeViewUiEvent.search
                )
            }
        }
    }
}

sealed interface HomeViewUiEvent {
    data class CategoryArticle(val category: String) : HomeViewUiEvent
    data class SearchArticle(val search: String) : HomeViewUiEvent
}

data class ArticleListUiState(
    val articleList: Flow<PagingData<Article>>? = emptyFlow(),
) {
    companion object {
        val default: ArticleListUiState = ArticleListUiState(articleList = emptyFlow())
    }
}

data class FilterArticleListUiState(
    val category: String = "",
    val query: String = "",
) {
    companion object {
        val default: FilterArticleListUiState = FilterArticleListUiState(
            query = "",
            category = "",
        )
    }
}