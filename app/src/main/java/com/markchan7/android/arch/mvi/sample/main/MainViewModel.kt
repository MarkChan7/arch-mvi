package com.markchan7.android.arch.mvi.sample.main

import androidx.lifecycle.viewModelScope
import com.markchan7.android.arch.mvi.MviViewModel
import com.markchan7.android.arch.mvi.sample.LCE.Error
import com.markchan7.android.arch.mvi.sample.LCE.Success
import com.markchan7.android.arch.mvi.sample.data.NewsItem
import com.markchan7.android.arch.mvi.sample.data.NewsRepository
import kotlinx.coroutines.launch

class MainViewModel : MviViewModel<MainViewState, MainViewEffect, MainViewEvent>() {

    private var count = 0
    private val repository = NewsRepository.getInstance()

    init {
        viewState = MainViewState(fetchStatus = FetchStatus.NotFetched, newsList = emptyList())
    }

    override fun process(viewEvent: MainViewEvent) {
        super.process(viewEvent)
        when (viewEvent) {
            is MainViewEvent.NewItemClicked -> newItemClicked(newsItem = viewEvent.newsItem)
            MainViewEvent.FabClicked -> fabClicked()
            MainViewEvent.FetchNews -> fetchNews()
            MainViewEvent.OnSwipeRefresh -> fetchNews()
        }
    }

    private fun newItemClicked(newsItem: NewsItem) {
        viewEffect = MainViewEffect.ShowSnackbar(message = newsItem.title)
    }

    private fun fabClicked() {
        count++
        viewEffect = MainViewEffect.ShowToast(message = "Fab click count $count")
    }

    private fun fetchNews() {
        viewState = viewState.copy(fetchStatus = FetchStatus.Fetching)
        viewModelScope.launch {
            when (val result = repository.getMockApiResponse()) {
                is Error -> {
                    viewState = viewState.copy(fetchStatus = FetchStatus.Fetched)
                    viewEffect = MainViewEffect.ShowToast(message = result.message)
                }
                is Success -> {
                    viewState = viewState.copy(
                        fetchStatus = FetchStatus.Fetched,
                        newsList = result.data
                    )
                }
            }
        }
    }
}
