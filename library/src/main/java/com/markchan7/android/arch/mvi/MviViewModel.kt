package com.markchan7.android.arch.mvi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val TAG = "MVI-ViewModel"

abstract class MviViewModel<STATE, EFFECT, EVENT> : ViewModel(), ViewModelContract<EVENT> {

    private val _viewStates: MutableLiveData<STATE> = MutableLiveData()
    val viewStates: LiveData<STATE> = _viewStates

    private var _viewState: STATE? = null
    protected var viewState: STATE
        get() = _viewState ?: throw UninitializedPropertyAccessException(
            """
            "viewState" was queried before being initialized. 
            You must initialize "viewState" inside init{} block
            """.trimIndent()
        )
        set(value) {
            if (MviConfig.enableLogs) {
                if (MviConfig.enableStackTrack) {
                    Thread.currentThread().stackTrace.let {
                        Log.d(
                            TAG,
                            """
                            setting viewState from
                                    ${it[3]}    
                                    ${it[4]}    
                                    ${it[5]}    
                                    : $value
                            """.trimIndent()
                        )
                    }
                } else {
                    Log.d(TAG, "setting viewState: $value")
                }
            }
            _viewState = value
            _viewStates.value = value!!
        }

    private val _viewEffects: SingleLiveEvent<EFFECT> = SingleLiveEvent()
    val viewEffects: SingleLiveEvent<EFFECT> = _viewEffects

    private var _viewEffect: EFFECT? = null
    protected var viewEffect: EFFECT
        get() = _viewEffect ?: throw UninitializedPropertyAccessException(
            """
            "viewEffect" was queried before being initialized.
            """.trimIndent()
        )
        set(value) {
            if (MviConfig.enableLogs) {
                if (MviConfig.enableStackTrack) {
                    Thread.currentThread().stackTrace.let {
                        Log.d(
                            TAG,
                            """
                            setting viewEffect from
                                    ${it[3]}    
                                    ${it[4]}    
                                    ${it[5]}    
                                    : $value
                            """.trimIndent()
                        )
                    }
                } else {
                    Log.d(TAG, "setting viewEffect: $value")
                }
            }
            _viewEffect = value
            _viewEffects.setValue(value!!)
        }

    override fun process(viewEvent: EVENT) {
        if (!_viewStates.hasObservers()) {
            throw NoObserverAttachedException(
                """
                No observer attached. In case of MviCustomView.startObserving() function need to be
                called manually.
                """.trimIndent()
            )
        }
        if (MviConfig.enableLogs) {
            Log.d(TAG, "process viewEvent: $viewEvent")
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (MviConfig.enableLogs) {
            Log.d(TAG, "onCleared")
        }
    }
}
