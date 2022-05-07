package com.markchan7.android.arch.mvi.sample.data

import com.markchan7.android.arch.mvi.sample.LCE
import com.markchan7.android.arch.mvi.sample.LCE.Error
import com.markchan7.android.arch.mvi.sample.LCE.Success
import com.markchan7.android.arch.mvi.sample.mockapi.MockApi

class NewsRepository {

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: NewsRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: NewsRepository().also { instance = it }
        }
    }

    suspend fun getMockApiResponse(): LCE<List<NewsItem>> {
        val articlesApiResult = try {
            MockApi.create().getLatestNews()
        } catch (e: Exception) {
            return Error(e)
        }

        articlesApiResult.articles?.let { list ->
            return Success(data = list)
        } ?: run {
            return Error("Failed to get News")
        }
    }
}
