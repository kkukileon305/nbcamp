package com.jess.nbcamp.challnge2.practice.signup

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.jess.nbcamp.challnge2.R
import com.jess.nbcamp.challnge2.practice.signup.SignUpValidExtension.includeAt
import com.jess.nbcamp.challnge2.practice.signup.SignUpValidExtension.includeSpecialCharacters
import com.jess.nbcamp.challnge2.practice.signup.SignUpValidExtension.includeUpperCase
import com.jess.nbcamp.challnge2.practice.signup.SignUpValidExtension.validEmailServiceProvider

class SignUpActivity : AppCompatActivity() {
	companion object {

		const val EXTRA_ENTRY_TYPE = "extra_entry_type"
		const val EXTRA_USER_ENTITY = "extra_user_entity"

		fun newIntent(
			context: Context,
			entryType: SignUpEntryType,
			entity: SignUpUserEntity
		): Intent = Intent(context, SignUpActivity()::class.java).apply {
			putExtra(EXTRA_ENTRY_TYPE, entryType.ordinal)
			putExtra(EXTRA_USER_ENTITY, entity)
		}
	}

	private val etName: EditText by lazy { findViewById(R.id.et_name) }
	private val tvNameError: TextView by lazy { findViewById(R.id.tv_name_error) }
	private val etEmail: EditText by lazy { findViewById(R.id.et_email) }
	private val etEmailProvider: EditText by lazy { findViewById(R.id.et_provider) }
	private val serviceProvider: Spinner by lazy { findViewById(R.id.service_provider) }
	private val tvEmailError: TextView by lazy { findViewById(R.id.tv_email_error) }
	private val etPassword: EditText by lazy { findViewById(R.id.et_password) }
	private val tvPasswordError: TextView by lazy { findViewById(R.id.tv_password_error) }
	private val etPasswordConfirm: EditText by lazy { findViewById(R.id.et_password_confirm) }
	private val tvPasswordConfirmError: TextView by lazy { findViewById(R.id.tv_password_confirm_error) }
	private val btConfirm: Button by lazy { findViewById(R.id.bt_confirm) }

	private val emailProvider
		get() = listOf(
			getString(R.string.sign_up_email_provider_gmail),
			getString(R.string.sign_up_email_provider_kakao),
			getString(R.string.sign_up_email_provider_naver),
			getString(R.string.sign_up_email_provider_direct)
		)

	private val entryType: SignUpEntryType by lazy {
		SignUpEntryType.getEntryType(
			intent?.getIntExtra(EXTRA_ENTRY_TYPE, 0)
		)
	}

	private val userEntity: SignUpUserEntity? by lazy {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			intent?.getParcelableExtra(EXTRA_USER_ENTITY, SignUpUserEntity::class.java)
		} else {
			intent?.getParcelableExtra(EXTRA_USER_ENTITY)
		}
	}

	private val viewModel by lazy { ViewModelProvider(this)[SignUpViewModel::class.java] }

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
		initViewModel()
	}

	private fun initView() {

		setTextChangedListener()

		// 이메일 서비스 제공자 처리
		setServiceProvider()

		// 버튼 이름 변경
		with(btConfirm) {
			setText(
				if (entryType == SignUpEntryType.UPDATE) {
					R.string.sign_up_update
				} else {
					R.string.sign_up_confirm
				}
			)

			setOnClickListener {
				if (viewModel.isConfirmEnable.value == true) {

				}
			}
		}

		setUserEntity()
	}

	private fun initViewModel() {
		viewModel.fields.observe(this) {
			// Check validation
			tvNameError.text = getMessageValidName()
			tvEmailError.text = getMessageValidEmail()
			tvEmailError.text = getMessageValidEmailProvider()

			with(tvPasswordError) {
				isEnabled = it[3].isNotBlank()
				text = if (it[3].isBlank()) {
					getString(SignUpErrorMessage.PASSWORD_HINT.message)
				} else {
					getMessageValidPassword()
				}
			}

			tvPasswordConfirmError.text = getMessageValidPasswordConfirm()

			viewModel.updateIsConfirmEnable(isConfirmButtonEnable())
		}

		viewModel.isConfirmEnable.observe(this) {
			btConfirm.isEnabled = it
		}
	}

	private fun setUserEntity() {
		etName.setText(userEntity?.name)
		etEmail.setText(userEntity?.email)

		val index = emailProvider.indexOf(userEntity?.emailService)
		// -1 일 경우 못찾음
		serviceProvider.setSelection(
			if (index < 0) {
				etEmailProvider.setText(userEntity?.emailService)
				emailProvider.lastIndex
			} else {
				index
			}
		)
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
				etEmailProvider.isVisible = position == serviceProvider.adapter.count - 1
			}
		}
	}

	private fun setTextChangedListener() {
		editTexts.forEachIndexed { index, editText ->
			editText.addTextChangedListener {
				viewModel.updateFields(index, it.toString())
			}
		}
	}

	private fun getMessageValidName(): String {
		val name = viewModel.fields.value?.get(0) ?: ""

		return getString(
			if (name.isBlank()) {
				SignUpErrorMessage.NAME
			} else {
				SignUpErrorMessage.PASS
			}.message
		)
	}

	private fun getMessageValidEmail(): String {
		val email = viewModel.fields.value?.get(1) ?: ""

		return getString(
			when {
				email.isBlank() -> SignUpErrorMessage.EMAIL_BLANK
				email.includeAt() -> SignUpErrorMessage.EMAIL_AT
				else -> SignUpErrorMessage.PASS
			}.message
		)
	}

	private fun getMessageValidEmailProvider(): String {
		return if (
			etEmailProvider.isVisible
			&& (etEmailProvider.text.toString().isBlank()
							|| (viewModel.fields.value?.get(2) ?: "").validEmailServiceProvider().not())
		) {
			getString(SignUpErrorMessage.EMAIL_SERVICE_PROVIDER.message)
		} else {
			getMessageValidEmail()
		}
	}

	private fun getMessageValidPassword(): String {
		val password = viewModel.fields.value?.get(3) ?: ""

		return getString(
			when {
				password.length < 10 -> SignUpErrorMessage.PASSWORD_LENGTH

				password.includeSpecialCharacters()
					.not() -> SignUpErrorMessage.PASSWORD_SPECIAL_CHARACTERS

				password.includeUpperCase().not() -> SignUpErrorMessage.PASSWORD_UPPER_CASE

				else -> SignUpErrorMessage.PASS
			}.message
		)
	}

	private fun getMessageValidPasswordConfirm(): String {
		val password = viewModel.fields.value?.get(3) ?: ""
		val passwordConfirm = viewModel.fields.value?.get(4) ?: ""

		return getString(
			if (password != passwordConfirm) {
				SignUpErrorMessage.PASSWORD_PASSWORD
			} else {
				SignUpErrorMessage.PASS
			}.message
		)
	}

	private fun isConfirmButtonEnable() = getMessageValidName().isBlank()
					&& getMessageValidEmail().isBlank()
					&& getMessageValidEmailProvider().isBlank()
					&& getMessageValidPassword().isBlank()
					&& getMessageValidPasswordConfirm().isBlank()
}