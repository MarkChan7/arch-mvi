package com.markchan7.android.arch.mvi

internal interface ViewModelContract<EVENT> {

    fun process(viewEvent: EVENT)
}
