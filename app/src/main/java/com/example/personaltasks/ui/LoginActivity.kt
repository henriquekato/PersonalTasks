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
            val signinCoroutineScope = CoroutineScope(Dispatchers.IO)
            signinCoroutineScope.launch {
                Firebase.auth.signInWithEmailAndPassword(
                    alb.emailLoginEt.text.toString(),
                    alb.passwordLoginEt.text.toString()
                ).addOnCompleteListener{
                    Toast.makeText(
                        this@LoginActivity,
                        "Logged in successfully",
                        Toast.LENGTH_SHORT
                    )
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(
                        this@LoginActivity,
                        "Login failed",
                        Toast.LENGTH_SHORT
                    )
                }
            }
        }
    }
}