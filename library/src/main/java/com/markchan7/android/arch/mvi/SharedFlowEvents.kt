package com.markchan7.android.arch.mvi

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableSharedFlow

suspend fun <T> SharedFlowEvents<T>.setEvents(
    vararg events: T
) {
    emit(events.toList())
}

fun <T> SharedFlowEvents<T>.observeEvent(
    lifecycleOwner: LifecycleOwner,
    action: (T) -> Unit
) {
    lifecycleOwner.lifecycleScope.launchWhenStarted {
        this@observeEvent.collect { events ->
            events.forEach { event ->
                action(event)
            }
        }
    }
}

typealias SharedFlowEvents<T> = MutableSharedFlow<List<T>>

fun <T> SharedFlowEvents(): SharedFlowEvents<T> {
    return MutableSharedFlow()
}
