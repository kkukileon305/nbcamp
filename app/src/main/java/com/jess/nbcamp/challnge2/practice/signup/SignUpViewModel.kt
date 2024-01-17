package com.jess.nbcamp.challnge2.practice.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {
	private val _fields = MutableLiveData(
		mutableListOf(
			"",
			"",
			"",
			"",
			""
		)
	)

	private val _isConfirmEnable = MutableLiveData(false)

	val fields: MutableLiveData<MutableList<String>>
		get() = _fields

	val isConfirmEnable: MutableLiveData<Boolean>
		get() = _isConfirmEnable

	fun updateFields(index: Int, value: String) {
		val newFields = _fields.value?.also {
			it[index] = value
		}

		_fields.value = newFields
	}

	fun updateIsConfirmEnable(value: Boolean) {
		_isConfirmEnable.value = value
	}
}