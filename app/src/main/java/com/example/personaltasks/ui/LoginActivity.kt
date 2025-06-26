package com.example.personaltasks.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val alb: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(alb.root)

        setSupportActionBar(alb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Login"

        alb.signUpBt.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        alb.signInBt.setOnClickListener {
            val email = alb.emailLoginEt.text.toString()
            val password = alb.passwordLoginEt.text.toString()
            if(email == "" || password == "") {
                Toast.makeText(
                    this@LoginActivity,
                    "Login failed",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val signInCoroutineScope = CoroutineScope(Dispatchers.IO)
            signInCoroutineScope.launch {
                Firebase.auth.signInWithEmailAndPassword(
                    email,
                    password
                ).addOnFailureListener {
                    Toast.makeText(
                        this@LoginActivity,
                        "Login failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnSuccessListener {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}