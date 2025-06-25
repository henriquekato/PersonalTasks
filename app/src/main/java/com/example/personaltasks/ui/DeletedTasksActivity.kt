package com.example.personaltasks.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.R
import com.example.personaltasks.databinding.ActivityDeletedTasksBinding

class DeletedTasksActivity : AppCompatActivity() {
    private val adtb: ActivityDeletedTasksBinding by lazy {
        ActivityDeletedTasksBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(adtb.root)

        setSupportActionBar(adtb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Deleted tasks"
    }
}