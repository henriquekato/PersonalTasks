package com.example.personaltasks.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
    }
}