package com.example.fotleague

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object LifecycleUtil {

    private val _restartAppEvent = MutableLiveData<Boolean>()
    val restartAppEvent: LiveData<Boolean> = _restartAppEvent

    fun onRestart() {
        // Trigger the event when login is successful
        _restartAppEvent.value = true
    }

    fun onSetFalse() {
        // Trigger the event when login is successful
        _restartAppEvent.value = false
    }
}