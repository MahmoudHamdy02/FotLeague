package com.example.fotleague

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object LifecycleUtil {

    private val _restartAppEvent = MutableLiveData<Boolean>()
    val restartAppEvent: LiveData<Boolean> = _restartAppEvent

    fun onSetRestartTrue() {
        _restartAppEvent.value = true
    }

    fun onSetRestartFalse() {
        _restartAppEvent.value = false
    }
}