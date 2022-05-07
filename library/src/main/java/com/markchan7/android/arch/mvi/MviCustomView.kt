package com.markchan7.android.arch.mvi

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

private const val TAG = "MVI-CustomView"

abstract class MviCustomView<STATE, EFFECT, EVENT, VM : MviViewModel<STATE, EFFECT, EVENT>> {

    abstract val viewModel: VM

    private val viewStateObserver = Observer<STATE> {
        if (MviConfig.enableLogs) {
            Log.d(TAG, "observed viewState: $it")
        }
        renderViewState(it)
    }

    private val viewEffectObserver = Observer<EFFECT> {
        if (MviConfig.enableLogs) {
            Log.d(TAG, "observed viewEffect: $it")
        }
        renderViewEffect(it)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun starObserving(lifecycleOwner: LifecycleOwner) {
        viewModel.viewStates.observe(lifecycleOwner, viewStateObserver)
        viewModel.viewEffects.observe(lifecycleOwner, viewEffectObserver)
    }

    abstract fun renderViewState(state: STATE)

    abstract fun renderViewEffect(state: EFFECT)
}
