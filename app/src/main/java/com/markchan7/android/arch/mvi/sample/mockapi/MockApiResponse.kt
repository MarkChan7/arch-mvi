package com.markchan7.android.arch.mvi.sample.mockapi

import com.markchan7.android.arch.mvi.sample.data.NewsItem

data class MockApiResponse(
    val articles: List<NewsItem>? = null
)
