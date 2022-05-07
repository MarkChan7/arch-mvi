package com.markchan7.android.arch.mvi

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

private const val TAG = "MVI-Activity"

abstract class MviActivity<STATE, EFFECT, EVENT, VM : MviViewModel<STATE, EFFECT, EVENT>> :
    AppCompatActivity() {

    abstract val viewModel: VM

    private val viewStateObserver by lazy {
        Observer<STATE> {
            if (MviConfig.enableLogs) {
                Log.d(TAG, "observed viewState: $it")
            }
            renderViewState(it)
        }
    }

    private val viewEffectObserver by lazy {
        Observer<EFFECT> {
            if (MviConfig.enableLogs) {
                Log.d(TAG, "observed viewEffect: $it")
            }
            renderViewEffect(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewState()
        viewModel.viewEffects.observe(this, viewEffectObserver)
    }

    protected open fun observeViewState() {
        viewModel.viewStates.observe(this, viewStateObserver)
    }

    open fun renderViewState(state: STATE) {
    }

    abstract fun renderViewEffect(effect: EFFECT)
}
