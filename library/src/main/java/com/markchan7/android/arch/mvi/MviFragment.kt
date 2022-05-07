package com.markchan7.android.arch.mvi

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

private const val TAG = "MVI-Fragment"

abstract class MviFragment<STATE, EFFECT, EVENT, VM : MviViewModel<STATE, EFFECT, EVENT>> :
    Fragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewStates.observe(viewLifecycleOwner, viewStateObserver)
        viewModel.viewEffects.observe(viewLifecycleOwner, viewEffectObserver)
    }

    abstract fun renderViewState(state: STATE)

    abstract fun renderViewEffect(state: EFFECT)
}
