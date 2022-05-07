package com.markchan7.android.arch.mvi.sample.main

import android.os.Bundle
import androidx.activity.viewModels
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.snackbar.Snackbar
import com.markchan7.android.arch.mvi.MviActivity
import com.markchan7.android.arch.mvi.observeState
import com.markchan7.android.arch.mvi.sample.R
import com.markchan7.android.arch.mvi.sample.data.NewsItem
import com.markchan7.android.arch.mvi.sample.databinding.ActivityMainBinding
import com.markchan7.android.arch.mvi.sample.toast

class MainActivity : MviActivity<MainViewState, MainViewEffect, MainViewEvent, MainViewModel>() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var adapter: BaseQuickAdapter<NewsItem, BaseViewHolder>

    override val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.newsRecyclerView.adapter = object : BaseQuickAdapter<NewsItem, BaseViewHolder>(
            R.layout.item_view_news
        ) {
            override fun convert(holder: BaseViewHolder, item: NewsItem) {
                holder.setText(R.id.title, item.title)
                    .setText(R.id.description, item.description)
            }
        }.apply {
            setOnItemClickListener { adapter, _, position ->
                viewModel.process(MainViewEvent.NewItemClicked(adapter.getItem(position) as NewsItem))
            }
            this@MainActivity.adapter = this
        }

        binding.newsSwipeRefreshLayout.setOnRefreshListener {
            viewModel.process(MainViewEvent.OnSwipeRefresh)
        }

        binding.startFab.setOnClickListener {
            viewModel.process(MainViewEvent.FabClicked)
        }
    }

    override fun observeViewState() {
        // 对整个对象监听
        // super.observeViewState()

        // 对属性单独监听
        viewModel.viewStates.run {
            observeState(this@MainActivity, MainViewState::fetchStatus) {
                if (it is FetchStatus.NotFetched) {
                    viewModel.process(MainViewEvent.FetchNews)
                }
                binding.newsSwipeRefreshLayout.isRefreshing = it is FetchStatus.Fetching
            }

            observeState(this@MainActivity, MainViewState::newsList) {
                adapter.setList(it)
            }
        }
    }

    override fun renderViewState(state: MainViewState) {
        when (state.fetchStatus) {
            FetchStatus.NotFetched -> {
                viewModel.process(MainViewEvent.FetchNews)
                binding.newsSwipeRefreshLayout.isRefreshing = false
            }
            FetchStatus.Fetching -> {
                binding.newsSwipeRefreshLayout.isRefreshing = true
            }
            FetchStatus.Fetched -> {
                binding.newsSwipeRefreshLayout.isRefreshing = false
            }
        }
        adapter.setList(state.newsList)
    }

    override fun renderViewEffect(effect: MainViewEffect) {
        when (effect) {
            is MainViewEffect.ShowSnackbar -> {
                Snackbar.make(binding.coordinatorLayout, effect.message, Snackbar.LENGTH_SHORT)
                    .show()
            }
            is MainViewEffect.ShowToast -> {
                toast(message = effect.message)
            }
        }
    }
}
