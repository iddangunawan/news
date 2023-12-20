package com.example.news.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.news.domain.model.Article
import com.example.news.domain.repository.TopHeadlinesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    topHeadlinesRepository: TopHeadlinesRepository,
) : ViewModel() {

    val articleListState: Flow<PagingData<Article>> =
        topHeadlinesRepository.getTopHeadlines(query = "").cachedIn(viewModelScope)

}