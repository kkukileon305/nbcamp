package com.jess.nbcamp.challnge2.practice.siginin

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.core.widget.addTextChangedListener
import com.jess.nbcamp.challnge2.R

class SignInActivity : AppCompatActivity() {

    private val etName: EditText by lazy {
        findViewById(R.id.et_name)
    }

    private val tvNameError: TextView by lazy {
        findViewById(R.id.tv_name_error)
    }

    private val etEmail: EditText by lazy {
        findViewById(R.id.et_email)
    }

    private val etEmailProvider: EditText by lazy {
        findViewById(R.id.et_provider)
    }

    private val serviceProvider: Spinner by lazy {
        findViewById(R.id.service_provider)
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
            etEmailProvider,
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

        serviceProvider.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val isVisibleProvider = position == serviceProvider.adapter.count - 1
                etEmailProvider.isVisible = isVisibleProvider
            }
        }
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
            etName -> tvNameError.text = getMessageValidName()
            etEmail -> tvEmailError.text = getMessageValidEmail()
            etEmailProvider -> tvEmailError.text = getMessageValidEmailProvider()
            else -> Unit
        }
    }

    private fun getMessageValidName(): String = if (etName.text.toString().isBlank()) {
        getString(R.string.sign_in_name_error)
    } else {
        ""
    }

    private fun getMessageValidEmail(): String {
        val text = etEmail.text.toString()
        return when {
            text.isBlank() -> getString(R.string.sign_in_email_error_blank)
            text.contains("@") -> getString(R.string.sign_in_email_error_at)
            else -> ""
        }
    }

    private fun getMessageValidEmailProvider(): String {
        val provider = Regex("[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        val text = etEmailProvider.text.toString()
        return if (
            etEmailProvider.isVisible
            && (etEmailProvider.text.toString().isBlank()
                    || provider.matches(text).not())
        ) {
            getString(R.string.sign_in_email_error_provider)
        } else {
            getMessageValidEmail()
        }
    }
}