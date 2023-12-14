package com.jess.nbcamp.challnge2.practice.siginin

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.jess.nbcamp.challnge2.R

class SignInActivity : AppCompatActivity() {

    private val serviceProvider: Spinner by lazy {
        findViewById(R.id.service_provider)
    }

    private val etName: EditText by lazy {
        findViewById(R.id.et_name)
    }

    private val tvNameError: TextView by lazy {
        findViewById(R.id.tv_name_error)
    }

    private val etEmail: EditText by lazy {
        findViewById(R.id.et_email)
    }

    private val tvEmailError: TextView by lazy {
        findViewById(R.id.tv_email_error)
    }

    private val etPassword: EditText by lazy {
        findViewById(R.id.et_password)
    }

    private val etPasswordConfirm: EditText by lazy {
        findViewById(R.id.et_password_confirm)
    }


    private val editTexts
        get() = listOf(
            etName,
            etEmail,
            etPassword,
            etPasswordConfirm
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in_activity)

        initView()
    }

    private fun initView() {

        setTextChangedListener()

        // focus out 처리
        setOnFocusChangedListener()

        // 이메일 서비스 제공자 처리
        setServiceProvider()
    }

    private fun setServiceProvider() {
        serviceProvider.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf(
                "gmail.com",
                "kakao.com",
                "naver.com",
                "직접입력"
            )
        )
    }

    private fun setTextChangedListener() {
        editTexts.forEach { editText ->
            editText.addTextChangedListener {
                editText.setErrorMessage()
            }
        }
    }

    private fun setOnFocusChangedListener() {
        editTexts.forEach { editText ->
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus.not()) {
                    editText.setErrorMessage()
                }
            }
        }
    }

    private fun EditText.setErrorMessage() {
        when (this) {
            etName -> {
                tvNameError.text = if (checkValidName()) {
                    ""
                } else {
                    getString(R.string.sign_in_name_error)
                }
            }

            etEmail -> {
                tvEmailError.text = if (checkValidEmail()) {
                    ""
                } else {
                    getString(R.string.sign_in_email_error)
                }
            }

            else -> Unit
        }
    }

    private fun checkValidName(): Boolean = etName.text.toString().isNotBlank()

    private fun checkValidEmail(): Boolean = etEmail.text.toString().isNotBlank()
}