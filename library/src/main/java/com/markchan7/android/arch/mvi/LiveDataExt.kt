package com.markchan7.android.arch.mvi

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import kotlin.reflect.KProperty1

/**
 * 监听一个属性
 */
fun <T, A> LiveData<T>.observeState(
    lifecycleOwner: LifecycleOwner,
    propA: KProperty1<T, A>,
    action: (A) -> Unit
) {
    map {
        propA.get(it)
    }.distinctUntilChanged().observe(lifecycleOwner) { a ->
        action.invoke(a)
    }
}

/**
 * 监听两个属性
 */
fun <T, A, B> LiveData<T>.observeState(
    lifecycleOwner: LifecycleOwner,
    propA: KProperty1<T, A>,
    propB: KProperty1<T, B>,
    action: (A, B) -> Unit
) {
    map {
        Pair(propA.get(it), propB.get(it))
    }.distinctUntilChanged().observe(lifecycleOwner) { (a, b) ->
        action.invoke(a, b)
    }
}

/**
 * 监听三个属性
 */
fun <T, A, B, C> LiveData<T>.observeState(
    lifecycleOwner: LifecycleOwner,
    propA: KProperty1<T, A>,
    propB: KProperty1<T, B>,
    propC: KProperty1<T, C>,
    action: (A, B, C) -> Unit
) {
    map {
        Triple(propA.get(it), propB.get(it), propC.get(it))
    }.distinctUntilChanged().observe(lifecycleOwner) { (a, b, c) ->
        action.invoke(a, b, c)
    }
}
