package com.markchan7.android.arch.mvi.sample.main

import com.markchan7.android.arch.mvi.sample.data.NewsItem

/**
 * View -> Event -> ViewModel { State / Effect }
 *  |                  |
 *  |<-----------------|
 */

sealed class MainViewEvent {
    data class NewItemClicked(val newsItem: NewsItem) : MainViewEvent()
    object FabClicked : MainViewEvent()
    object OnSwipeRefresh : MainViewEvent()
    object FetchNews : MainViewEvent()
}

data class MainViewState(
    val fetchStatus: FetchStatus,
    val newsList: List<NewsItem>
)

sealed class FetchStatus {
    object Fetching : FetchStatus()
    object Fetched : FetchStatus()
    object NotFetched : FetchStatus()
}

sealed class MainViewEffect {
    data class ShowSnackbar(val message: String) : MainViewEffect()
    data class ShowToast(val message: String) : MainViewEffect()
}
