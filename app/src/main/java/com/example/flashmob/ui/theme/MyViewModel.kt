package com.example.flashmob.ui.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel() : ViewModel() {
    private val _isOn = MutableLiveData<Boolean>(false)
    private val _discoOn = MutableLiveData<Boolean>(false)
    private val _speed = MutableLiveData<Long>(1000)
    val isOn: LiveData<Boolean>
        get() = _isOn
    val discoOn: LiveData<Boolean>
        get() = _discoOn
    val speed: LiveData<Long>
        get() = _speed

    fun updateTorchState(isOn: Boolean) {
        _isOn.value = isOn
    }
    fun updateDiscoState(discoOn: Boolean) {
        _discoOn.value = discoOn
    }
    fun updateSpeed(speed: Long) {
        _speed.value = speed
    }
}
