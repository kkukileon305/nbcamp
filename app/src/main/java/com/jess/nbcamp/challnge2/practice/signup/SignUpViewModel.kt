package com.jess.nbcamp.challnge2.practice.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {

    private val _test: MutableLiveData<String> = MutableLiveData()
    val test: LiveData<String> get() = _test

    fun test(text: String) {
        _test.value = text
    }

}