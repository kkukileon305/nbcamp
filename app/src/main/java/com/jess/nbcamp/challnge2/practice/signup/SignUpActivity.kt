package com.jess.nbcamp.challnge2.practice.signup

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.jess.nbcamp.challnge2.R
import com.jess.nbcamp.challnge2.practice.signup.SignUpValidExtension.includeAt
import com.jess.nbcamp.challnge2.practice.signup.SignUpValidExtension.includeSpecialCharacters
import com.jess.nbcamp.challnge2.practice.signup.SignUpValidExtension.includeUpperCase
import com.jess.nbcamp.challnge2.practice.signup.SignUpValidExtension.validEmailServiceProvider

class SignUpActivity : AppCompatActivity() {

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

    private val tvPasswordError: TextView by lazy {
        findViewById(R.id.tv_password_error)
    }

    private val etPasswordConfirm: EditText by lazy {
        findViewById(R.id.et_password_confirm)
    }

    private val tvPasswordConfirmError: TextView by lazy {
        findViewById(R.id.tv_password_confirm_error)
    }

    private val btConfirm: Button by lazy {
        findViewById(R.id.bt_confirm)
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
        setContentView(R.layout.sign_up_activity)

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
                getString(R.string.sign_up_email_provider_gmail),
                getString(R.string.sign_up_email_provider_kakao),
                getString(R.string.sign_up_email_provider_naver),
                getString(R.string.sign_up_email_provider_direct)
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
                setConfirmButtonEnable()
            }
        }
    }

    private fun setOnFocusChangedListener() {
        editTexts.forEach { editText ->
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus.not()) {
                    editText.setErrorMessage()
                    setConfirmButtonEnable()
                }
            }
        }
    }

    private fun EditText.setErrorMessage() {
        when (this) {
            etName -> tvNameError.text = getMessageValidName()
            etEmail -> tvEmailError.text = getMessageValidEmail()
            etEmailProvider -> tvEmailError.text = getMessageValidEmailProvider()
            etPassword -> {
                with(tvPasswordError) {
                    isEnabled = etPassword.text.toString().isNotBlank()
                    text = if (etPassword.text.toString().isBlank()) {
                        getString(SignUpErrorMessage.PASSWORD_HINT.message)
                    } else {
                        getMessageValidPassword()
                    }
                }
            }

            etPasswordConfirm -> tvPasswordConfirmError.text = getMessageValidPasswordConfirm()

            else -> Unit
        }
    }

    private fun getMessageValidName(): String = getString(
        if (etName.text.toString().isBlank()) {
            SignUpErrorMessage.NAME
        } else {
            SignUpErrorMessage.PASS
        }.message
    )

    private fun getMessageValidEmail(): String {
        val text = etEmail.text.toString()
        return getString(
            when {
                text.isBlank() -> SignUpErrorMessage.EMAIL_BLANK
                text.includeAt() -> SignUpErrorMessage.EMAIL_AT
                else -> SignUpErrorMessage.PASS
            }.message
        )
    }

    private fun getMessageValidEmailProvider(): String {
        val text = etEmailProvider.text.toString()
        return if (
            etEmailProvider.isVisible
            && (etEmailProvider.text.toString().isBlank()
                    || text.validEmailServiceProvider().not())
        ) {
            getString(SignUpErrorMessage.EMAIL_SERVICE_PROVIDER.message)
        } else {
            getMessageValidEmail()
        }
    }

    private fun getMessageValidPassword(): String {
        val text = etPassword.text.toString()
        return getString(
            when {
                text.length < 10 -> SignUpErrorMessage.PASSWORD_LENGTH

                text.includeSpecialCharacters()
                    .not() -> SignUpErrorMessage.PASSWORD_SPECIAL_CHARACTERS

                text.includeUpperCase().not() -> SignUpErrorMessage.PASSWORD_UPPER_CASE

                else -> SignUpErrorMessage.PASS
            }.message
        )
    }

    private fun getMessageValidPasswordConfirm(): String =
        getString(
            if (etPassword.text.toString() != etPasswordConfirm.text.toString()) {
                SignUpErrorMessage.PASSWORD_PASSWORD
            } else {
                SignUpErrorMessage.PASS
            }.message
        )

    private fun setConfirmButtonEnable() {
        btConfirm.isEnabled = getMessageValidName().isBlank()
                && getMessageValidEmail().isBlank()
                && getMessageValidEmailProvider().isBlank()
                && getMessageValidPassword().isBlank()
                && getMessageValidPasswordConfirm().isBlank()
    }
}