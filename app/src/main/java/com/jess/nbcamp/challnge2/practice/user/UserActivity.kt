package com.jess.nbcamp.challnge2.practice.user

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.jess.nbcamp.challnge2.R
import com.jess.nbcamp.challnge2.practice.signup.SignUpActivity
import com.jess.nbcamp.challnge2.practice.signup.SignUpEntryType
import com.jess.nbcamp.challnge2.practice.signup.SignUpUserEntity

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_activity)

        findViewById<Button>(R.id.bt_update).setOnClickListener {
            startActivity(
                SignUpActivity.newIntent(
                    context = this@UserActivity,
                    entryType = SignUpEntryType.UPDATE,
                    entity = SignUpUserEntity(
                        name = "스파르타",
                        email = "hello",
                        emailService = "r.com"
                    )
                )
            )
        }
    }
}